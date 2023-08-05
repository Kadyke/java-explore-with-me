package ru.practicum;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.service.CategoryService;
import ru.practicum.service.UserService;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.service.EventService;

@SpringBootTest
public class StatsServerTest {
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    private final EasyRandom random = new EasyRandom();

    @Test
    @DirtiesContext
    void test() {
        User user = random.nextObject(User.class);
        userService.createUser(user);
        Category category = random.nextObject(Category.class);
        categoryService.createCategory(category);
        Event event = random.nextObject(Event.class);
        event.setCategory(category);
        event.setInitiator(user);
    }
}
