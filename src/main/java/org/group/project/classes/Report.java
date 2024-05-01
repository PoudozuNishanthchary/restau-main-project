package org.group.project.classes;

import org.group.project.classes.auxiliary.DataFileStructure;
import org.group.project.classes.auxiliary.DataManager;
import org.group.project.exceptions.TextFileNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * This class standardises all reports in a clear and informative manner for
 * viewing.
 *
 * @author azmi_maz
 */
public class Report {
    protected int reportId;
    protected String reportType;
    protected String reportData;
    protected String generatedBy;
    protected LocalDate generatedOnDate;
    protected LocalTime generatedOnTime;

    /**
     * This constructor creates a report with updated data from database.
     *
     * @param reportId        - the unique report id.
     * @param reportType      - the type of the report.
     * @param reportData      - the main content of the report.
     * @param generatedBy     - the user who generated the report.
     * @param generatedOnDate - the date the report was generated.
     * @param generatedOnTime - the time the report was generated.
     */
    public Report(
            int reportId,
            String reportType,
            String reportData,
            String generatedBy,
            LocalDate generatedOnDate,
            LocalTime generatedOnTime
    ) {
        this.reportId = reportId;
        this.reportType = reportType;
        this.reportData = reportData;
        this.generatedBy = generatedBy;
        this.generatedOnDate = generatedOnDate;
        this.generatedOnTime = generatedOnTime;
    }

    /**
     * This constructor creates a new report with timestamp.
     *
     * @param reportId   - the unique report id.
     * @param reportType - the type of the report.
     * @param reportData - the main content of the report.
     * @param user       - the user who generated the report.
     */
    public Report(int reportId, String reportType, String reportData,
                  User user) {
        this.reportId = reportId;
        this.reportType = reportType;
        this.reportData = reportData;
        this.generatedBy = user.getDataForListDisplay();
        this.generatedOnDate = LocalDate.now();
        this.generatedOnTime = LocalTime.now();
    }

    /**
     * This constructor creates a report with basic info.
     *
     * @param reportType - the type of the report.
     * @param user       - the user who generated the report.
     * @throws TextFileNotFoundException - if text file is non-existent.
     */
    public Report(String reportType, User user)
            throws TextFileNotFoundException {

        this.reportId = getNewReportId();
        this.reportType = reportType;
        this.generatedBy = user.getDataForListDisplay();
        this.generatedOnDate = LocalDate.now();
        this.generatedOnTime = LocalTime.now();

    }

    /**
     * Getter method to get the report id.
     *
     * @return the report id.
     */
    public int getReportId() {
        return reportId;
    }

    /**
     * This method gets a new unique id for a report.
     *
     * @return a new unique id.
     * @throws TextFileNotFoundException - if text file is non-existent.
     */
    public int getNewReportId() throws TextFileNotFoundException {
        try {
            List<String> listOfData = DataManager
                    .allDataFromFile("REPORTS");
            int lastId;
            String lastReport = listOfData.getLast();
            String[] reportDetails = lastReport.split(",");
            int reportIdIndex = DataFileStructure
                    .getIndexColOfUniqueId("REPORTS");
            lastId = Integer.parseInt(reportDetails[reportIdIndex]);
            if (lastId > -1) {
                lastId++;
                return lastId;
            }
            return lastId;

        } catch (TextFileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Getter method to get the report type.
     *
     * @return the report type.
     */
    public String getReportType() {
        return reportType;
    }

    /**
     * Getter method to get who generated the report.
     *
     * @return the staff who made the report.
     */
    public String getGeneratedBy() {
        return generatedBy;
    }

    /**
     * Getter method to get the date the report was made.
     *
     * @return the report date.
     */
    public LocalDate getGeneratedOnDate() {
        return generatedOnDate;
    }

    /**
     * This method gets the report date in dd/mm/yyyy format.
     *
     * @return the report date in the desired format.
     */
    public String getGeneratedOnDateInFormat() {
        return getGeneratedOnDate().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    /**
     * This method gets the report date in database compatible format.
     *
     * @return the report date in the desired format.
     */
    public String getGeneratedOnDateForDatabase() {
        return getGeneratedOnDate().format(
                DateTimeFormatter.ofPattern("yyyy-M-d"));
    }

    /**
     * Getter method to get the time the report was made.
     *
     * @return the report time.
     */
    public LocalTime getGeneratedOnTime() {
        return generatedOnTime;
    }

    /**
     * This method gets the report time in hh:mm a format.
     *
     * @return the report time in the desired format.
     */
    public String getGeneratedOnTimeInFormat() {
        return getGeneratedOnTime().format(
                DateTimeFormatter.ofPattern("hh:mm a"));
    }

    /**
     * This method gets the report time in database compatible format.
     *
     * @return the report time in the desired format.
     */
    public String getGeneratedOnTimeForDatabase() {
        return getGeneratedOnTime().format(
                DateTimeFormatter.ofPattern("H-m"));
    }

    /**
     * Getter method to get the report data.
     *
     * @return the report data.
     */
    public String getReportData() {
        return reportData;
    }

    /**
     * Setter method to set the main message of the report.
     *
     * @param newReport - the main message.
     */
    public void setReportData(String newReport) {
        this.reportData = newReport;
    }

    /**
     * This method appends a report with another report.
     *
     * @param additionalReport - the additional report data.
     */
    public void appendReportData(Report additionalReport) {
        this.reportData += " " + additionalReport.getReportData();
    }

    /**
     * This method gets the report data in database compatible format.
     *
     * @return the report in the desired format.
     */
    public String getReportDataForDatabase() {
        String compactedReportData = "";
        List<String> splitUp =
                List.of(getReportData().split(
                        System.lineSeparator()));

        for (int i = 0; i < splitUp.size(); i++) {
            String currentLine = splitUp.get(i);
            if (i == splitUp.size() - 1) {
                compactedReportData += currentLine;
            } else {
                compactedReportData += currentLine + ";";
            }
        }
        return compactedReportData.replaceAll(",", "}");
    }

    /**
     * This method formats the report to a readable format for user.
     *
     * @param compactedData - the data string taken from the database.
     * @return the string in readable format.
     */
    public String uncompactReportData(
            String compactedData
    ) {
        String uncompactedData = "";
        List<String> rawDataArray = List.of(compactedData.split(";"));
        for (String line : rawDataArray) {
            uncompactedData += line + System.lineSeparator();
        }

        return uncompactedData.replaceAll("}", ",");
    }

    /**
     * This method generates the report in a standardised format.
     *
     * @return a standardised report for the UI.
     */
    public String generateReport() {
        return String.format(
                "Report no. %d" + System.lineSeparator() +
                        "Type: %s" + System.lineSeparator() +
                        "Generated by: %s" + System.lineSeparator() +
                        "Date: %s %s" + System.lineSeparator() +
                        " " + System.lineSeparator() +
                        "%s",
                getReportId(),
                getReportType(),
                getGeneratedBy(),
                getGeneratedOnDateInFormat(),
                getGeneratedOnTimeInFormat(),
                uncompactReportData(getReportData())
        );
    }

    /**
     * This displays the report info when printed out.
     *
     * @return the report info string.
     */
    @Override
    public String toString() {
        return String.format(
                "No.%s, %s, %s %s",
                getReportId(),
                getReportType(),
                getGeneratedOnDateInFormat(),
                getGeneratedOnTimeInFormat()
        );
    }

}
