package com.bugtracker.backend.util;

import com.bugtracker.backend.entity.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TestData {
    private static final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    public static List<Comment> generateComment(){
        return Stream.of(
                "Message1",
                "very cool",
                "On it",
                "will be done",
                "nice Idea",
                "Something something",
                "oooga"

        ).map(description -> {
            return new Comment(description);
        }).collect(Collectors.toList());
    }

    public static List<Priority> generatePriority(){
        return Stream.of(
                "Urgent",
                "Important",
                "Focus",
                "Needed",
                "Back Log"
        ).map(importance -> {
            return new Priority(importance, threadLocalRandom.nextInt(1,importance.length()));
        }).collect(Collectors.toList());
    }

    public static List<Role> generateRole(){
        return Stream.of(
                "Dev",
                "Tester",
                "Maintainer",
                "Guest"
        ).map(role ->{
            return new Role(role, threadLocalRandom.nextInt(1,3));

        }).collect(Collectors.toList());
    }

    public static List<Tag> generateTag(){
        return Stream.of(
                "Bug",
                "Improvement",
                "Developer",
                "Needed",
                "Good Task",
                "Error",
                "Missing"

        ).map(description -> {
            return new Tag(description);
        }).collect(Collectors.toList());
    }

    public static List<User> generateUser(){
        List<User> users = new ArrayList<>();

        users.add(new User("John","email@something","0771117","123"));
        users.add(new User("Emma","email@something2","0777888","123"));
        users.add(new User("Dave","email@something3","07799997","123"));
        users.add(new User("Leo","email@something4","07700007","123"));
        users.add(new User("Sam","email@something5","07711117","123"));

        return users;
    }

    public static List<Project> generateProjects(List<Priority> priorities){
        List<String> description = new ArrayList<>();
        description.add("Its a cool description");
        description.add("Back again");
        description.add("Sample desc");
        description.add("Anything");

        return Stream.of(
                "First project Title",
                "Some more projeect",
                "Very cool project",
                "Etc project"
        ).map(title -> createProject(description,priorities,title)).collect(Collectors.toList());

    }

    public static Project createProject(List<String> desc, List<Priority> priorities, String title){
        Date today = new Date();
        today.setHours(0);

        Project project = new Project(
                title,
                generateRandomDesc(desc),
                true,today,generateRandomPriority(priorities)
        );
        return project;
    }

    public static List<TicketHistory> generateTicketHistory(List<Ticket> tickets){
        List<String> description = new ArrayList<>();
        description.add("Its a cool description");
        description.add("Back again");
        description.add("Sample desc");
        description.add("Anything");

        return Stream.of(
                "Last title",
                "An old title",
                "not current title",
                "title was updated"
        ).map(title -> createTicketHistory(description,title,tickets)).collect(Collectors.toList());
    }

    public static TicketHistory createTicketHistory(List<String> desc, String title, List<Ticket> tickets){
        Date today = new Date();
        today.setHours(0);
        TicketHistory ticketHistory = new TicketHistory(
                generateRandomTicket(tickets),
                title,generateRandomDesc(desc),today
        );
        return ticketHistory;
    }

    public static List<Ticket> generateTickets(List<User> users, List<Project> projects, List<Priority> priorities){
        //List<String> title;
        List<String> description = new ArrayList<>();
        description.add("Its a cool description");
        description.add("Back again");
        description.add("Sample desc");
        description.add("Anything");

        List<Date> ticketCreated;
        List<Date> ticketDeadline;
        List<TicketStatus> ticketStatuses;


        return Stream.of(
                "This Needs done",
                "This is bugged",
                "Enchancements made to method",
                "Something is missing"
        ).map(title -> createTicket(title,users,projects,priorities,description)).collect(Collectors.toList());
    }

    public static Ticket createTicket(String title,List<User> users, List<Project> projects, List<Priority> priorities,
                                      List<String> description){

        Date today = new Date();
        today.setHours(0);
        Ticket ticket = new Ticket(
                generateRandomProject(projects),
                generateRandomUser(users),
                title,
                generateRandomDesc(description),
                today,
                today,
                TicketStatus.CLOSED,
                generateRandomUser(users),
                generateRandomPriority(priorities)
        );
        return ticket;
    }


    private static Project generateRandomProject(List<Project> projects){
        return projects.get(threadLocalRandom.nextInt(projects.size()));
    }
    private static User generateRandomUser(List<User> users){
        return users.get(threadLocalRandom.nextInt(users.size()));
    }
    private static Priority generateRandomPriority(List<Priority> priorities){
        return priorities.get(threadLocalRandom.nextInt(priorities.size()));
    }
    private static String generateRandomDesc(List<String> desc){
        return desc.get(threadLocalRandom.nextInt(desc.size()));
    }
    private static Ticket generateRandomTicket(List<Ticket> tickets){
        return tickets.get(threadLocalRandom.nextInt(tickets.size()));
    }

}
