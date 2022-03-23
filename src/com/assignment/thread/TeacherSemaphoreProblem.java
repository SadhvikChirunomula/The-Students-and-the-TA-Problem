package com.assignment.thread;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class TeacherSemaphoreProblem {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter no.of students: ");
        Shared.numberOfStudents = sc.nextInt();

        Semaphore studentReadySemaphore = new Semaphore(Shared.studentReady);
        Semaphore seatsAvailableWithTeacherSemaphore = new Semaphore(Shared.seatsAvailableWithTeacher);
        Semaphore teacherReadySemaphore = new Semaphore(Shared.teacherReady);

        ClassRoom teacher = new ClassRoom("teacher", 1, studentReadySemaphore, seatsAvailableWithTeacherSemaphore, teacherReadySemaphore);

        teacher.start();
        System.out.println("Starting Teacher thread...");

//         Starting students after 5 seconds
//        Thread.sleep(2000);

        for (int i = 1; i <= Shared.numberOfStudents; i++) {
            ClassRoom student = new ClassRoom("student", i, studentReadySemaphore, seatsAvailableWithTeacherSemaphore, teacherReadySemaphore);
            student.start();
        }


//        teacher.interrupt();

    }
}

class Shared {
    static int studentReady = 0;
    static int seatsAvailableWithTeacher = 1;
    static int seatsAvailableInQueue = 3;
    static int teacherReady = 0;
    static int studentsInQueue = 0;
    static int numberOfStudents;
}

class ClassRoom extends Thread {
    String threadName;
    int id;
    Semaphore studentReadySemaphore;
    Semaphore seatsAvailableWithTeacherSemaphore;
    Semaphore teacherReadySemaphore;

    public ClassRoom(String threadName, int id, Semaphore studentReadySemaphore, Semaphore seatsAvailableWithTeacherSemaphore, Semaphore teacherReadySemaphore) {
        this.threadName = threadName;
        this.id = id;
        this.studentReadySemaphore = studentReadySemaphore;
        this.seatsAvailableWithTeacherSemaphore = seatsAvailableWithTeacherSemaphore;
        this.teacherReadySemaphore = teacherReadySemaphore;
    }

    public void run() {
        if (this.threadName.equalsIgnoreCase("teacher")) {
            try {
                callTeacher();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (threadName.equalsIgnoreCase("student")) {
            try {
                callStudent();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void callTeacher() throws InterruptedException {
        System.out.println("[TA THREAD] Total students to deal with is " + Shared.numberOfStudents);
        System.out.println("[SEMAPHORE] TA gets the lock!");
        System.out.println("[TA THREAD] TA is in the office.");


        while (!Thread.interrupted()) {
            System.out.println("[TA THREAD] Waiting students = " + Shared.studentsInQueue + ", Students in the office = " + (Shared.seatsAvailableWithTeacher == 1 ? 0 : 1));


            studentReadySemaphore.acquire();
            --Shared.studentReady;

            seatsAvailableWithTeacherSemaphore.acquire();
            --Shared.seatsAvailableWithTeacher;

            ++Shared.seatsAvailableInQueue;
            System.out.println("[TA THREAD] TA is helping a student...");

            // let us say teacher is helping student for 2 seconds..
            Thread.sleep(2000);
            System.out.println("[TA THREAD] Waiting students = " + (3 - Shared.seatsAvailableInQueue) + ", Students in the office = " + (Shared.seatsAvailableWithTeacher == 1 ? 0 : 1));
            teacherReadySemaphore.release();
            ++Shared.teacherReady;

            seatsAvailableWithTeacherSemaphore.release();
            ++Shared.seatsAvailableWithTeacher;

            System.out.println("[TA THREAD] TA is done with the student!");
        }

        System.out.println("No student found in last 10 seconds, hence ending");

    }

    public void callStudent() throws InterruptedException {
        int i = 0;
        while (i < 1) {
            System.out.println("[STUDENT THREAD] Student " + this.id + " is coming!");
            boolean queueHasCapacity = (Shared.studentsInQueue >= 0 && Shared.studentsInQueue < 3);

            if (queueHasCapacity) {
                ++i;
                ++Shared.studentsInQueue;

                seatsAvailableWithTeacherSemaphore.acquire();
                --Shared.seatsAvailableWithTeacher;
                System.out.println("[STUDENT THREAD] Student " + this.id + " is seating on the waiting chair #" + Shared.studentsInQueue);

                --Shared.studentsInQueue;
                System.out.println("[SEMAPHORE] Student " + this.id + " gets the lock!");

                System.out.println("[STUDENT THREAD] Student " + this.id + " wakes up TA and gets help.");
                studentReadySemaphore.release();
                ++Shared.studentReady;

                System.out.println("[TA THREAD] Waiting students = " + Shared.studentsInQueue + ", Students in the office = " + (Shared.seatsAvailableWithTeacher == 1 ? 0 : 1));

                seatsAvailableWithTeacherSemaphore.release();
                System.out.println("[SEMAPHORE] Student " + this.id + " releases the lock!");

                ++Shared.seatsAvailableWithTeacher;
                Shared.seatsAvailableInQueue--;

                teacherReadySemaphore.acquire();
                --Shared.teacherReady;
            } else {
                seatsAvailableWithTeacherSemaphore.release();
                ++Shared.seatsAvailableWithTeacher;
                System.out.println("[STUDENT THREAD] Student " + this.id + " is waiting.");

            }

        }
    }
}
