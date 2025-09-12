package ssu.cromi.teamit.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ssu.cromi.teamit.domain.Stack;
import ssu.cromi.teamit.repository.StackRepository;

import java.lang.reflect.Constructor;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final StackRepository stackRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeStacks();
    }

    private void initializeStacks() {
        String[] stackTags = {
            "Java", "Spring Boot", "React", "TypeScript", "JavaScript",
            "Python", "Node.js", "Vue.js", "Angular", "MySQL", 
            "PostgreSQL", "MongoDB", "Redis", "Docker", "Kubernetes"
        };

        for (String tag : stackTags) {
            if (!stackRepository.findByTag(tag).isPresent()) {
                try {
                    Constructor<Stack> constructor = Stack.class.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    Stack stack = constructor.newInstance();
                    stack.setTag(tag);
                    stack.setIcon(tag.toLowerCase() + "-icon.png");
                    stackRepository.save(stack);
                    log.info("기술스택 생성: {}", tag);
                } catch (Exception e) {
                    log.error("기술스택 생성 실패: {}", tag, e);
                }
            }
        }
    }
}