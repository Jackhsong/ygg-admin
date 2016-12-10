package com.ygg.admin.filter;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.filter.stat.StatFilterContext;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.StatementExecuteType;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.stat.JdbcDataSourceStat;
import com.alibaba.druid.stat.JdbcSqlStat;
import com.alibaba.druid.support.json.JSONWriter;
import com.alibaba.druid.support.profile.Profiler;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.SQLException;

/**
 * @author lorabit
 * @since 16-5-23
 */
public class CustomStatFilter extends StatFilter {
    private final static Logger LOG                        = Logger.getLogger(CustomStatFilter.class);
    static  {
        LOG.setAdditivity(false);
    }

    @Override
    protected void statementExecuteBatchAfter(StatementProxy statement, int[] result) {
        internalAfterStatementExecute(statement, false, result);
    }

    @Override
    protected void statementExecuteAfter(StatementProxy statement, String sql, boolean firstResult) {
        internalAfterStatementExecute(statement, firstResult);
    }

    @Override
    protected void statementExecuteQueryAfter(StatementProxy statement, String sql, ResultSetProxy resultSet) {
        internalAfterStatementExecute(statement, true);
    }

    @Override
    protected void statementExecuteUpdateAfter(StatementProxy statement, String sql, int updateCount) {
        internalAfterStatementExecute(statement, false, updateCount);
    }

    private final void internalAfterStatementExecute(StatementProxy statement, boolean firstResult,
                                                     int... updateCountArray) {
        final long nowNano = System.nanoTime();
        final long nanos = nowNano - statement.getLastExecuteStartNano();

        JdbcDataSourceStat dataSourceStat = statement.getConnectionProxy().getDirectDataSource().getDataSourceStat();
        dataSourceStat.getStatementStat().afterExecute(nanos);

        final JdbcSqlStat sqlStat = statement.getSqlStat();

        if (sqlStat != null) {
            sqlStat.incrementExecuteSuccessCount();

            sqlStat.decrementRunningCount();
            sqlStat.addExecuteTime(statement.getLastExecuteType(), firstResult, nanos);
            statement.setLastExecuteTimeNano(nanos);
            if ((!statement.isFirstResultSet()) && statement.getLastExecuteType() == StatementExecuteType.Execute) {
                try {
                    int updateCount = statement.getUpdateCount();
                    sqlStat.addUpdateCount(updateCount);
                } catch (SQLException e) {
                    LOG.error("getUpdateCount error", e);
                }
            } else {
                for (int updateCount : updateCountArray) {
                    sqlStat.addUpdateCount(updateCount);
                    sqlStat.addFetchRowCount(0);
                    StatFilterContext.getInstance().addUpdateCount(updateCount);
                }
            }

            long millis = nanos / (1000 * 1000);
            if (millis >= slowSqlMillis) {
                String slowParameters = buildSlowParameters(statement);
                sqlStat.setLastSlowParameters(slowParameters);

                if (logSlowSql) {
                    LOG.error("slow sql " + millis + " millis. \n" + statement.getLastExecuteSql() + "\n"
                            + slowParameters);
                }
            }
        }

        String sql = statement.getLastExecuteSql();
        StatFilterContext.getInstance().executeAfter(sql, nanos, null);

        Profiler.release(nanos);
    }

    private String buildSlowParameters(StatementProxy statement) {
        JSONWriter out = new JSONWriter();

        out.writeArrayStart();
        for (int i = 0, parametersSize = statement.getParametersSize(); i < parametersSize; ++i) {
            JdbcParameter parameter = statement.getParameter(i);
            if (i != 0) {
                out.writeComma();
            }
            if (parameter == null) {
                continue;
            }

            Object value = parameter.getValue();
            if (value == null) {
                out.writeNull();
            } else if (value instanceof String) {
                String text = (String) value;
                if (text.length() > 100) {
                    out.writeString(text.substring(0, 97) + "...");
                } else {
                    out.writeString(text);
                }
            } else if (value instanceof Number) {
                out.writeObject(value);
            } else if (value instanceof java.util.Date) {
                out.writeObject(value);
            } else if (value instanceof Boolean) {
                out.writeObject(value);
            } else if (value instanceof InputStream) {
                out.writeString("<InputStream>");
            } else if (value instanceof NClob) {
                out.writeString("<NClob>");
            } else if (value instanceof Clob) {
                out.writeString("<Clob>");
            } else if (value instanceof Blob) {
                out.writeString("<Blob>");
            } else {
                out.writeString('<' + value.getClass().getName() + '>');
            }
        }
        out.writeArrayEnd();

        return out.toString();
    }

}
