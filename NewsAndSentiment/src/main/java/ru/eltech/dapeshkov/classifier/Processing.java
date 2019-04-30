package ru.eltech.dapeshkov.classifier;

import ru.eltech.dapeshkov.news.JSONProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Stream;

public class Processing {

    private static final Map<Pair, Double> likelihood = new HashMap<>(); //concurency not needed final field safe published
    private static final Map<String, Double> prior_probability = new HashMap<>(); //concurency not needed final field safe published
    private static final Set<String> hash = new HashSet<>();
    private static int count = 0;
    private static int n;
    static final private String[] category = {"positive", "negative", "neutral"};

    static {
        try (Stream<String> lines = new BufferedReader(new InputStreamReader(Processing.class.getResourceAsStream("/stopwatch.txt"))).lines()) {
            lines.forEach(hash::add);
        }
    }

    private Processing() {

    }

    private static class Pair {
        final String word;
        final String category;

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Pair pair = (Pair) o;
            return Objects.equals(getWord(), pair.getWord()) &&
                    Objects.equals(getCategory(), pair.getCategory());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getWord(), getCategory());
        }

        Pair(final String word, final String category) {
            this.word = word;
            this.category = category;
        }

        private String getWord() {
            return word;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "word='" + word + '\'' +
                    ", category='" + category + '\'' +
                    '}';
        }

        private String getCategory() {
            return category;
        }
    }

    private static String[] parse(final String str, final int n) {
        String[] res = str.toLowerCase().split("[^\\p{L}]+");
        res = Arrays.stream(res).filter(t -> !hash.contains(t)).distinct().toArray(String[]::new);

        res = ngram(res, n);

        return res;
    }

    private static String[] ngram(final String[] arr, final int n) {
        String[] res = new String[arr.length - n + 1];
        for (int i = 0; i < arr.length - n + 1; i++) {
            final StringBuilder str = new StringBuilder();
            for (int j = 0; j < n; j++) {
                str.append(arr[i + j]).append(" ");
            }
            res[i] = str.toString();
        }
        return res;
    }

    static public void train(final int n) {

        Processing.n = n;

        JSONProcessor.Train[] arr = null;

        try (InputStream in = Processing.class.getResourceAsStream("/train1.json")) {
            arr = JSONProcessor.parse(in, JSONProcessor.Train[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        count = Objects.requireNonNull(arr).length;

        Arrays.stream(arr).unordered().forEach(i -> {
            final String[] strings = parse(i.getText(), Processing.n);
            Arrays.stream(strings).unordered().forEach((str) -> likelihood.compute(new Pair(str, i.getSentiment()), (k, v) -> (v == null) ? 1 : v + 1));
            prior_probability.compute(i.getSentiment(), (k, v) -> (v == null) ? 1 : v + 1);
        });
    }

    private static Double classify_cat(final String str, final String... arr) {
        return Math.log(prior_probability.get(str) / count) +
                Arrays.stream(arr).unordered()
                        .mapToDouble(value -> (likelihood.getOrDefault(new Pair(value, str), 0d) + 1) / (prior_probability.get(str) + likelihood.size()))
                        .reduce(0, (left, right) -> left + Math.log(right));
    }

    static public String sentiment(final String str) {
        final String[] arr = parse(str, Processing.n);

        return Arrays.stream(category).unordered()
                .max(Comparator.comparingDouble(o -> classify_cat(o, arr)))
                .get();
    }

    public static void main(final String[] args) {
        train(2);
    }
}