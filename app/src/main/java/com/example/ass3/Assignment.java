package com.example.ass3;

public class Assignment {

    private int assignmentId;
    private int courseId;
    private String assignmentTitle;
    private int grade;

    public Assignment(int assignmentId, int courseId, String assignmentTitle, int grade) {
        this.assignmentId = assignmentId;
        this.courseId = courseId;
        this.assignmentTitle = assignmentTitle;
        this.grade = grade;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
