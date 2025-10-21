package org.skypro.Reminder005Bot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    List<NotificationTask> findByNotificationDateTime(LocalDateTime notificationDateTime);

    @Query("SELECT nt FROM NotificationTask nt WHERE nt.notificationDateTime = :dateTime")
    List<NotificationTask> findTasksForNotification(@Param("dateTime") LocalDateTime dateTime);
}