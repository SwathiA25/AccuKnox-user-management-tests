package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {

    private static ExtentReports extent;

    public static ExtentReports getExtent() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("reports/AccuKnox_Report.html");
            spark.config().setDocumentTitle("AccuKnox - User Management Report");
            spark.config().setReportName("User Management Automation");
            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
        return extent;
    }
}
