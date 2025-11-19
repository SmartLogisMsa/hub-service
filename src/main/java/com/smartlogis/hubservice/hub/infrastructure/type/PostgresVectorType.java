package com.smartlogis.hubservice.hub.infrastructure.type;

import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.jdbc.BasicBinder;
import org.hibernate.type.descriptor.jdbc.BasicExtractor;
import org.hibernate.type.descriptor.jdbc.JdbcType;

import java.sql.*;
import java.util.Arrays;

public class PostgresVectorType implements JdbcType {

    @Override
    public int getJdbcTypeCode() {
        return SqlTypes.OTHER;
    }

    @Override
    public <X> BasicBinder<X> getBinder(JavaType<X> javaType) {

        return new BasicBinder<>(javaType, this) {

            @Override
            protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
                    throws SQLException {

                float[] vector = (float[]) value;

                // pgvector 형식: [1.23, 4.56]
                String vectorString = "[" + Arrays.toString(vector)
                        .replace("[", "")
                        .replace("]", "") + "]";

                st.setObject(index, vectorString, Types.OTHER);
            }

            @Override
            protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
                    throws SQLException {

                float[] vector = (float[]) value;

                String vectorString = "[" + Arrays.toString(vector)
                        .replace("[", "")
                        .replace("]", "") + "]";

                st.setObject(name, vectorString, Types.OTHER);
            }
        };
    }

    @Override
    public <X> BasicExtractor<X> getExtractor(JavaType<X> javaType) {

        return new BasicExtractor<>(javaType, this) {

            @Override
            protected X doExtract(ResultSet rs, int index, WrapperOptions options)
                    throws SQLException {

                String vectorString = rs.getString(index);
                if (vectorString == null) return null;

                return parseVectorString(vectorString);
            }

            @Override
            protected X doExtract(CallableStatement st, int index, WrapperOptions options)
                    throws SQLException {

                String vectorString = st.getString(index);
                if (vectorString == null) return null;

                return parseVectorString(vectorString);
            }

            @Override
            protected X doExtract(CallableStatement st, String name, WrapperOptions options)
                    throws SQLException {

                String vectorString = st.getString(name);
                if (vectorString == null) return null;

                return parseVectorString(vectorString);
            }

            private X parseVectorString(String vectorString) {

                // "[37.4742, 127.12362]" → "37.4742, 127.12362"
                vectorString = vectorString
                        .replace("[", "")
                        .replace("]", "")
                        .trim();

                String[] parts = vectorString.split(",");

                float[] values = new float[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    values[i] = Float.parseFloat(parts[i].trim());
                }

                return (X) values;
            }
        };
    }
}
