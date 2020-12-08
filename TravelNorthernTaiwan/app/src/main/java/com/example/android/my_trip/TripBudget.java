package com.example.android.my_trip;

import android.util.Log;

public class TripBudget {
    private int Budget, Accommodation, Food, Souvenirs, Tickets, Others;

    public TripBudget(){
        Budget = Accommodation = Food =  Souvenirs = Tickets = Others = 0;
    }

    public TripBudget(int Budget, int Accommodation, int Food, int Souvenirs,
                      int Tickets, int Others){
        this.Budget = Budget;
        this.Accommodation = Accommodation;
        this.Food = Food;
        //this.Shopping = Shopping;
        this.Souvenirs = Souvenirs;
        this.Tickets = Tickets;
        this.Others = Others;
    }
    //Get
    public int getBudget(){
        return Budget;
    }

    public int getAccommodation(){return Accommodation;}

    public int getFood(){return Food;}

    //public int getShopping(){return Shopping;}

    public int getSouvenir(){return Souvenirs;}

    public int getTicket(){return Tickets;}

    public int getOther(){return Others;}

    //Set
    public void setBudget(int money){ Budget += money; }

    public void setAccommodation(int money){ Accommodation += money;}

    public void setFood(int money){ Food += money;}

    /*public void setShopping(int money){ Shopping += money;}*/

    public void setSouvenir(int money){ Souvenirs += money;}

    public void setTicket(int money){ Tickets += money;}

    public void setOther(int money){ Others += money;}

    /*public void setTripBudgetInfo(int money, String expenseCategory){
        switch(expenseCategory){
            case "Budget":
                setBudget(money);
                break;
            case "Accommodation":
                setAccommodation(money);
                break;
            case "Food":
                setFood(money);
                break;
            *//*case "Shopping":
                setShopping(money);
                break;*//*
            case "Souvenirs":
                setSouvenir(money);
                break;
            case "Tickets":
                setTicket(money);
                break;
            case "Others":
                setOther(money);
                break;
            default:
                break;
        }
    }*/
}
