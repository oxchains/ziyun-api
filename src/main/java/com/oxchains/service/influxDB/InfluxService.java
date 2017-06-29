package com.oxchains.service.influxDB;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by song on 2017/6/26.
 */
public class InfluxService  {
    private String urlInfluxDB;
    private String usernameInfluxDB;
    private String passwordInfluxDB;
    private volatile InfluxDB db;
    private ReentrantLock lock;
    private String dbName;

    public InfluxService(String urlInfluxDB, String usernameInfluxDB, String passwordInfluxDB, String dbName) {
        this.dbName = dbName;
        this.urlInfluxDB = urlInfluxDB;
        this.usernameInfluxDB = usernameInfluxDB;
        this.passwordInfluxDB = passwordInfluxDB;
        this.lock = new ReentrantLock();
    }

    public Optional<InfluxDB> get() {
        if(!this.ping()) {
            this.lock.lock();

            Optional var2;
            try {
                if(!this.ping()) {
                    this.db = InfluxDBFactory.connect(this.urlInfluxDB, this.usernameInfluxDB, this.passwordInfluxDB);
                }
                return Optional.ofNullable(this.db);
            } catch (Exception var6) {
                var6.printStackTrace();
                var2 = Optional.empty();
            } finally {
                this.lock.unlock();
            }

            return var2;
        } else {
            return Optional.ofNullable(this.db);
        }
    }

    private boolean ping() {
        try {
            return this.db != null && this.db.ping() != null;
        } catch (Exception var2) {
            return false;
        }
    }

    public void write(Point point) {
        this.get().ifPresent((influxDB) -> {
            influxDB.write(this.dbName, "autogen", point);
        });
    }

    public void createDatabase(String database) {
        this.get().ifPresent((influxDB) -> {
            influxDB.createDatabase(database);
        });
    }

    public Optional<QueryResult> query(String request) {
        if(request != null && !request.isEmpty()) {
            return this.get().map((influxDB) -> {
                return influxDB.query(new Query(request, this.dbName));
            });
        } else {
            throw new IllegalArgumentException("request string can\'t be null or empty");
        }
    }

    public void deleteMeasurementByTags(String measurement, Map<String, String> tags) throws InfluxDestroyerException {
        if(tags != null && !tags.isEmpty() && measurement != null && !measurement.isEmpty()) {
            String command = "DELETE FROM \"" + measurement + "\" WHERE 1 = 1";

            Map.Entry e;
            for(Iterator query = tags.entrySet().iterator(); query.hasNext(); command = command + " AND \"" + (String)e.getKey() + "\" = \'" + (String)e.getValue() + "\'") {
                e = (Map.Entry)query.next();
            }

            Query query1 = new Query(command, this.dbName, true);

            try {
                this.get().ifPresent((influxDB) -> {
                    QueryResult.Result result = (QueryResult.Result)influxDB.query(query1).getResults().get(0);
                    if(result.hasError()) {
                        throw new RuntimeException(result.getError());
                    }
                });
            } catch (RuntimeException var6) {
                throw new InfluxDestroyerException(var6);
            }
        } else {
            throw new IllegalArgumentException("arguments can\'t by null or empty");
        }
    }
}
