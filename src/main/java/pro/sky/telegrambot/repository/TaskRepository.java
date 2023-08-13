package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.entity.Task;

import java.time.LocalDateTime;
import java.util.Collection;
public interface TaskRepository extends JpaRepository<Task, Long> {

    Collection<Task> findAllByDateTime(LocalDateTime localDateTime);
}
