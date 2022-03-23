# The Students and the TA Problem

A university computer science department has a teaching assistant (TA) who helps
undergraduate students with their programming assignments. The TA’s office is rather
small and has room for only one desk with a chair and computer. There are three chairs in
the hallway outside the office where students can sit and wait if the TA is currently
helping another student. When there are no students who need help, the TA sits at the
desk and takes a nap. If a student arrives and finds the TA sleeping, the student must
awaken the TA to ask for help. If a student arrives and finds the TA currently helping
another student, the student sits on one of the chairs in the hallway and waits. If no chairs
are available, the student will come back at a later time. Using threads, implement a
solution that coordinates the activities of the TA and the students. Details for this
assignment are provided below.

## Problem Statement

Using threads (Section 4.4), begin by creating n students. Each will run as a separate
thread. The TA will run as a separate thread as well. Student threads will seek help from
the TA. If the TA is available, they will obtain help. Otherwise, they will either sit on a
chair in the hallway or, if no chairs are available, will seek help at a later time (random
period of time). If a student arrives and notices that the TA is sleeping, the student must
notify the TA using a semaphore. When the TA finishes helping a student, the TA must
check to see if there are students waiting for help in the hallway. If so, the TA must help
each of these students in turn. If no students are present, the TA may return to napping.
Simulating the TA providing help to a student is to have the appropriate threads sleep for
a random period of time.

* Programming Language used : Java 
* Java Version : 11
* No additional Dependencies added

## How to run

1. Clone this Repo and open in IntellIJ and right click on the main class and hit run.
2. It will ask to enter no.of students count. Enter an integer value and the code will be execute.

!!Happy Learning!!