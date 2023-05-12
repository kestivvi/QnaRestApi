INSERT INTO USERS (ID, EMAIL, FIRSTNAME, LASTNAME, PASSWORD)
VALUES (1, 'admin@email.com', 'Anna', 'Kaźmierczak', '$2a$10$3N5djNG34ceRhJcbYiPLHewRkuunAmYUiKkB8KuXI4VxO9eNUWmjq'),
       (2, 'moderator@email.com', 'Jan', 'Kowalski', '$2a$10$AD07BI8UKv.uTLGa3s9e2OaTrTyr3WRyMS62nqZsLf7TcpRRDrady'),
       (3, 'user1@email.com', 'Jan', 'Nowak', '$2a$10$O5quyDK5dW9yz83F5YbGyu4pe0HeERCw4a7nkYhjas2TmlZvWPSfq'),
       (4, 'user2@email.com', 'Mateusz', 'Zmyśliński', '$2a$10$tXmmfMMY7NmakuuevTtyhe4dg9bNx7wqKcbIE638C9yWs/hQZC6LO');

INSERT INTO USER_X_ROLES (USER_ID, ROLE)
VALUES (1, 0),
       (1, 1),
       (1, 2),
       (2, 0),
       (2, 1),
       (3, 0),
       (4, 0);


INSERT INTO TAGS (ID, NAME)
VALUES  (1, 'memory-retention'),
        (2, 'meditation'),
        (3, 'procrastination');

INSERT INTO QUESTIONS (ID, TITLE, DESCRIPTION, AUTHOR_ID)
VALUES  (1, 'What are some effective ways to improve memory retention?', 'I often find myself struggling to remember important information, whether its for exams or daily tasks. Are there any proven methods or techniques that can help enhance memory retention? Im looking for practical strategies that I can implement easily into my routine.', 3),
        (2, 'What are the benefits of practicing mindfulness meditation?', 'Ive heard a lot about mindfulness meditation and its positive effects on mental well-being, but Im curious about the specific benefits it can provide. Can someone explain the advantages of practicing mindfulness meditation and how it can improve various aspects of life?', 4),
        (3, 'What are some effective ways to overcome procrastination?', 'Procrastination often gets in the way of productivity and can be a source of frustration. Are there any practical strategies or techniques that have been proven effective in overcoming procrastination? Im looking for tips to help me break this habit and stay focused on my tasks and goals.', 1);

INSERT INTO QUESTIONS_X_TAGS (QUESTION_ID, TAG_ID)
VALUES  (1, 1),
        (2, 2),
        (3, 3);

INSERT INTO ANSWERS (ID, CONTENT, AUTHOR_ID, QUESTION_ID)
VALUES  (1, 'There are several effective ways to improve memory retention. One strategy is to engage in regular physical exercise, as it increases blood flow to the brain and promotes the growth of new neurons. Additionally, practicing active recall by testing yourself on the information you want to remember has been shown to enhance long-term memory. Another technique is to create associations or visual imagery to link new information with existing knowledge. Finally, getting enough sleep is crucial for memory consolidation, so make sure you prioritize quality sleep each night.', 3, 1),
        (2, 'Practicing mindfulness meditation offers numerous benefits. Firstly, it helps reduce stress and anxiety by allowing individuals to focus on the present moment and cultivate a sense of calm. Additionally, regular mindfulness practice can enhance emotional regulation, leading to improved mood and a greater ability to handle difficult emotions. Mindfulness also enhances self-awareness, enabling individuals to recognize negative thought patterns and develop a more positive mindset. Moreover, studies have shown that mindfulness meditation can improve concentration and cognitive abilities, leading to better decision-making and problem-solving skills. Overall, incorporating mindfulness meditation into your routine can have a positive impact on mental, emotional, and cognitive well-being.', 4, 2),
        (3, 'Overcoming procrastination requires conscious effort and adopting effective strategies. One approach is to break tasks into smaller, manageable chunks, making them feel less overwhelming. Setting specific, realistic deadlines and using productivity tools or apps to track progress can help create a sense of accountability. Additionally, eliminating distractions by creating a conducive work environment, such as turning off notifications or finding a quiet space, can enhance focus. Utilizing time management techniques like the Pomodoro Technique—working for focused intervals and taking short breaks—can increase productivity. Its also helpful to identify and address underlying reasons for procrastination, such as fear of failure or perfectionism, through self-reflection and self-compassion. By implementing these strategies and cultivating discipline, it is possible to overcome procrastination and improve productivity.', 3, 3);
