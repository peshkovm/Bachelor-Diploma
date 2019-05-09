package ru.eltech.mapeshkov.mlib;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;

/**
 * Enum that contains schemes used for prediction
 */
public enum Schemes {
    SCHEMA_NOT_LABELED(new StructField[]{
            new StructField("company", DataTypes.StringType, false, Metadata.empty()),
            new StructField("sentiment", DataTypes.StringType, false, Metadata.empty()),
            new StructField("date", DataTypes.TimestampType, false, Metadata.empty()),
            new StructField("today_stock", DataTypes.DoubleType, false, Metadata.empty()),
            //new StructField("tomorrow_stock", DataTypes.DoubleType, false, Metadata.empty()),
    }),
    SCHEMA_LABELED(new StructField[]{
            new StructField("company", DataTypes.StringType, false, Metadata.empty()),
            new StructField("sentiment", DataTypes.StringType, false, Metadata.empty()),
            new StructField("date", DataTypes.StringType, false, Metadata.empty()),
            new StructField("today_stock", DataTypes.DoubleType, false, Metadata.empty()),
            new StructField("label", DataTypes.DoubleType, true, Metadata.empty()),
    }),
    SCHEMA_WINDOWED() {
        @Override
        public void setWindowWidth(int windowWidth) {
            this.windowWidth = windowWidth;

            ////////////fill schema///////////////////
            ArrayList<StructField> structFieldList = new ArrayList<>();
            StructField[] structFields = new StructField[2 * windowWidth + 1];

            for (int i = windowWidth - 1; i >= 0; i--) {
                if (i != 0) {
                    structFieldList.add(new StructField("sentiment_" + i, DataTypes.StringType, false, Metadata.empty()));
                    structFieldList.add(new StructField("stock_" + i, DataTypes.DoubleType, false, Metadata.empty()));
                } else {
                    structFieldList.add(new StructField("sentiment_today", DataTypes.StringType, false, Metadata.empty()));
                    structFieldList.add(new StructField("stock_today", DataTypes.DoubleType, false, Metadata.empty()));
                    structFieldList.add(new StructField("label", DataTypes.DoubleType, true, Metadata.empty()));
                }
            }

            structFields = structFieldList.toArray(structFields);
            //////////////////////////////////////////

            structType = new StructType(structFields);
        }

        @Override
        public int getWindowWidth() {
            return this.windowWidth;
        }
    };

    protected StructType structType;

    protected int windowWidth = 0;

    Schemes() {
        this(null);
    }

    Schemes(StructField[] structFields) {
        this.structType = new StructType(structFields);
    }

    /**
     * Returns scheme's type
     *
     * @return
     */
    public StructType getScheme() {
        return structType;
    }

    /**
     * Returns width of sliding window
     *
     * @return
     */
    public int getWindowWidth() {
        return 0;
    }

    /**
     * Sets width of sliding window
     *
     * @param windowWidth
     */
    public void setWindowWidth(int windowWidth) {
    }
}