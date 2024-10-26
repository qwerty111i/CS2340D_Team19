package com.example.sprint1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DestinationJUnitViewModel {

    private String location;
    private String startDate;
    private String endDate;
    private String locationError;
    private String durationError;
    private String startDateError;
    private String endDateError;
    private String dateError;
    private boolean validInputs;
    private boolean validCalcInputs;
    private String duration;
    private String startVacationDate;
    private String endVacationDate;

    public void setTravelDetails(String location, String startDate, String endDate) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;

        boolean validLocation = checkInput(location);
        boolean validDates = checkDates(startDate, endDate);

        if (!validLocation) {
            locationError = "Invalid Location Input!";
        } else {
            locationError = null;
        }

        if (!validDates) {
            dateError = "Invalid Dates!";
        } else {
            dateError = null;
        }

        validInputs = validLocation && validDates;
    }

    public void calculateVacationTime(String duration, String startDate, String endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");

        boolean isDurationValid = isValidDuration(duration);
        boolean isStartDateValid = isValidDate(startDate, formatter);
        boolean isEndDateValid = isValidDate(endDate, formatter);
        boolean validDates = checkDates(startDate, endDate);

        if (!isStartDateValid && !isEndDateValid && !isDurationValid) {
            durationError = "Invalid Duration!";
            startDateError = "Invalid Start Date!";
            endDateError = "Invalid End Date!";
            validCalcInputs = false;
        } else if (!isStartDateValid && !isEndDateValid) {
            durationError = null;
            startDateError = "Invalid Start Date!";
            endDateError = "Invalid End Date!";
            validCalcInputs = false;
        } else if (!isDurationValid && !isEndDateValid) {
            durationError = "Invalid Duration!";
            startDateError = null;
            endDateError = "Invalid End Date!";
            validCalcInputs = false;
        } else if (!isStartDateValid && !isDurationValid) {
            startDateError = "Invalid Start Date!";
            durationError = "Invalid Duration!";
            endDateError = null;
            validCalcInputs = false;
        } else if (isStartDateValid && isEndDateValid && !validDates) {
            startDateError = "Invalid Start Date!";
            endDateError = "Invalid End Date!";
            if (!isDurationValid) {
                durationError = "Invalid Duration!";
            } else {
                durationError = null;
            }
            validCalcInputs = false;
        } else if (!isDurationValid) {
            try {
                Date start = formatter.parse(startDate);
                Date end = formatter.parse(endDate);
                int dur = (int) ((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
                this.duration = "" + dur;
                this.startVacationDate = startDate;
                this.endVacationDate = endDate;
                startDateError = null;
                durationError = null;
                endDateError = null;
                validCalcInputs = true;
            } catch (ParseException e) {
                // Error handling
            }
        } else if (!isEndDateValid) {
            try {
                Date start = formatter.parse(startDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start);
                calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(duration));
                Date end = calendar.getTime();
                this.endVacationDate = formatter.format(end);
                this.startVacationDate = startDate;
                startDateError = null;
                durationError = null;
                endDateError = null;
                validCalcInputs = true;
            } catch (ParseException e) {
                // Error handling
            }
        } else if (!isStartDateValid) {
            try {
                Date end = formatter.parse(endDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(end);
                calendar.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(duration));
                Date start = calendar.getTime();
                this.startVacationDate = formatter.format(start);
                this.endVacationDate = endDate;
                startDateError = null;
                durationError = null;
                endDateError = null;
                validCalcInputs = true;
            } catch (ParseException e) {
                // Error handling
            }
        } else {
            try {
                Date start = formatter.parse(startDate);
                Date end = formatter.parse(endDate);
                int dur = (int) ((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
                int d2 = Integer.parseInt(duration);
                if (dur != d2) {
                    durationError = "Duration isn't correctly calculated!";
                    startDateError = "Invalid Start Date!";
                    endDateError = "Invalid End Date!";
                    validCalcInputs = false;
                } else {
                    this.startVacationDate = startDate;
                    this.endVacationDate = endDate;
                    startDateError = null;
                    durationError = null;
                    endDateError = null;
                    validCalcInputs = true;
                }
            } catch (ParseException e) {
                // Error handling
            }
        }
    }

    private boolean isValidDate(String dateStr, SimpleDateFormat formatter) {
        if (dateStr == null) {
            return true;
        }
        try {
            formatter.setLenient(false);
            formatter.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidDuration(String duration) {
        if (duration == null) {
            return true;
        }
        try {
            Integer.parseInt(duration);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean checkInput(String input) {
        return input != null && !input.isEmpty();
    }

    public boolean checkDates(String date1, String date2) {
        if (date1 != null && date2 != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy", Locale.getDefault());
                Date startDate = sdf.parse(date1);
                Date endDate = sdf.parse(date2);
                return !startDate.after(endDate);
            } catch (ParseException e) {
                return false;
            }
        }
        return false;
    }

    public String getLocationError() {
        return locationError;
    }

    public String getDurationError() {
        return durationError;
    }

    public String getDateError() {
        return dateError;
    }

    public String getStartDateError() {
        return startDateError;
    }

    public String getEndDateError() {
        return endDateError;
    }

    public String getDuration() {
        return duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartVacationDate() {
        return startVacationDate;
    }

    public String getEndVacationDate() {
        return endVacationDate;
    }

    public boolean areInputsValid() {
        return validInputs;
    }

    public boolean areCalcInputsValid() {
        return validCalcInputs;
    }
}
