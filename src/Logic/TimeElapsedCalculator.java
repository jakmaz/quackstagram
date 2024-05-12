package Logic;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * TimeElapsedCalculator
 */
public class TimeElapsedCalculator {

  public static String getElapsedTime(LocalDateTime notificationTimestamp) {
    LocalDateTime now = LocalDateTime.now();
    long days = ChronoUnit.DAYS.between(notificationTimestamp, now);
    long hours = ChronoUnit.HOURS.between(notificationTimestamp, now) % 24;
    long minutes = ChronoUnit.MINUTES.between(notificationTimestamp, now) % 60;

    StringBuilder timeSince = new StringBuilder();
    if (days > 0) {
      timeSince.append(days).append(" days");
    }
    if (hours > 0) {
      if (timeSince.length() > 0)
        timeSince.append(", ");
      timeSince.append(hours).append(" hours");
    }
    if (minutes > 0 && days == 0) { // Only show minutes if there are no days counted
      if (timeSince.length() > 0)
        timeSince.append(", ");
      timeSince.append(minutes).append(" minutes");
    }

    if (timeSince.length() == 0) {
      return "Just now";
    } else {
      return timeSince.append(" ago").toString();
    }
  }
}
