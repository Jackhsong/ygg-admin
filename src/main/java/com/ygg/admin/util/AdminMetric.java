package com.ygg.admin.util;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

/**
 * @author lorabit
 * @since 16-5-9
 */
public class AdminMetric {
    public static final MetricRegistry core = new MetricRegistry();
    public static Meter meter = null;
    public static Meter errMeter = null;
    public static Timer timer = null;

    static {
        meter = core.meter("admin");
        errMeter = core.meter("admin" + "-err");
        timer = core.timer("admin" + "-timer");
    }
}
