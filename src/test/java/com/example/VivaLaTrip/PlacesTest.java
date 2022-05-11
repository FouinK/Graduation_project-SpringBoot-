package com.example.VivaLaTrip;

public class PlacesTest {

//    String places;
//    int places_count;
    int stay;
    int days;
    double x;
    double y;
    double slope;
    String where;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "PlacesTest{" +
                "stay=" + stay +
                ", days=" + days +
                ", x=" + x +
                ", y=" + y +
                ", slope=" + slope +
                ", where='" + where + '\'' +
                '}';
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public double getSlope() {
        return slope;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }




    public int getStay() {
        return stay;
    }

    public void setStay(int stay) {
        this.stay = stay;
    }


}
