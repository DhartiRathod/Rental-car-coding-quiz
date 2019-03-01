package rental_car_question_answers;

//Author : Dharti Rathod
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.path.json.JsonPath;

public class Rental_cars {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ObjectMapper mapper = new ObjectMapper();
		File f=new File("/Users/krunalshah/Documents/workspace/RentalCar/src/rental_car_question_answers/rental_car.json");
		JsonPath jsonpath= JsonPath.from(f);// retrieving the json responsefrom the file
		int NumCars=jsonpath.get("Car.size()");//no. of elements in the car node
		
		//Question 1: 
		//Retrieving and displaying blue Tesla cars 
		System.out.println("Question 1: Print all the blue Teslas received in the web service response. Also print the notes");
		System.out.println("Answer 1: Blue Tesla Cars along with Notes are: ");
		for(int CarCount=0;CarCount<NumCars;CarCount++) {
			if(jsonpath.getString("Car["+CarCount+"].metadata.Color").equalsIgnoreCase("blue") &&
					jsonpath.getString("Car["+CarCount+"].make").equalsIgnoreCase("tesla")) {
				
				System.out.println(jsonpath.getString("Car["+CarCount+"].metadata.Color")+ " " +(jsonpath.getString("Car["+CarCount+"].make")) + "car # " + CarCount);
				System.out.println("Notes for BlueTesla # " +CarCount + " is: "+jsonpath.getString("Car["+CarCount+"].metadata.Notes"));
				System.out.println();
			}
		}
		
		//Question 2: 
		//getting the minimum price 
		System.out.println("Question 2: Return all cars which have the lowest per day rental cost for both cases:  \n  a. Price only.  \n  b. Price after discounts");
		System.out.println("Answer 2a.");
		int lowPrice=jsonpath.get("Car.perdayrent.Price.min()");
		System.out.println("Lower per day rental cost without discount is: " + lowPrice );
		System.out.println("Details of low Car rental cost without discount is: "+
				jsonpath.get("Car.findAll{it->it.perdayrent.Price == "+lowPrice+"}") );
		System.out.println();
		
		//Retrieving the cars which has lowest rate per day based on price and discount
		System.out.println("Answer 2b. Rental Price after discounts");
		int price, discount;
		List<Float> ListCarsDiscount = new ArrayList<Float>();
		float lowPriceDiscount, CalPriceDiscount;
		//calculating discounts for all the cars
		for(int CarCount=0;CarCount<NumCars;CarCount++) {
			price=jsonpath.get("Car["+CarCount+"].perdayrent.Price");//price of the car per day
			discount=jsonpath.get("Car["+CarCount+"].perdayrent.Discount");//discount of the car per day
			CalPriceDiscount=price-(price*discount/100);//calculating the rate after discount
			ListCarsDiscount.add(CalPriceDiscount);// list of all the rates after discount
		}
		//Finding lowest pricing and discount in all the cars
		lowPriceDiscount=Collections.min(ListCarsDiscount);
		System.out.println("Cars which have the lowest per day rental cost based Price after discounts");
		//finding and displaying all the cars which has less price per day based on price and discount  
		for(int CarCount=0;CarCount<NumCars;CarCount++) {
			price=jsonpath.get("Car["+CarCount+"].perdayrent.Price");
			discount=jsonpath.get("Car["+CarCount+"].perdayrent.Discount");
			CalPriceDiscount=price-(price*discount/100);
			if(CalPriceDiscount==lowPriceDiscount)//checking for minimum discount 
				
			
         System.out.println(jsonpath.get("Car["+CarCount+"].make") + " car, Vin: "+ (jsonpath.get("Car["+CarCount+"].vin") + " , color : " +(jsonpath.get("Car["+CarCount+"].metadata.Color") +" car has LOWEST PRICE after discounts = " + CalPriceDiscount + "$")));
			
			//System.out.println(jsonpath.get("Car["+CarCount+"]")+"\n");// retrieving the cars based on the lowest rate after discount
		}
		
		System.out.println("\n Question 3: Find the highest revenue generating car. year over year maintenance cost + depreciation is the total expense per car for the full year for the rental car company.The objective is to find those cars that produced the highest profit in the last year");
		System.out.println("Answer 3 : ");
		//Finding the highest revenue generated car
		List<Float> listRevenueCars=new ArrayList<Float>();
		int  Days;
		//Calculating the revenues of all the cars
		for(int CarCount=0;CarCount<NumCars;CarCount++) {
			Days=jsonpath.get("Car["+CarCount+"].metrics.rentalcount.yeartodate");//no.of rental days
			listRevenueCars.add(ListCarsDiscount.get(CarCount)*Days);//adding revenues to list
		}
		//Finding maximum revenue in the list of revenues
		int RevenueCarIndex=listRevenueCars.indexOf(Collections.max(listRevenueCars));
		System.out.println("The highest revenue generating car is: "+ jsonpath.getString("Car["+RevenueCarIndex+"].make") + " Car (Color-"+jsonpath.getString("Car["+RevenueCarIndex+"].metadata.Color")+" Vin#-"+jsonpath.getString("Car["+RevenueCarIndex+"].vin")+")");
		System.out.println("The highest revenue generating car details are as below : "+ jsonpath.get("Car["+RevenueCarIndex+"]"));
		System.out.println();
		
		//Calculating highest profit car
		List<Float> listProfit=new ArrayList<Float>();
		float yoyCost,depreciation;
		for(int CarCount=0;CarCount<NumCars;CarCount++) {
			yoyCost=jsonpath.get("Car["+CarCount+"].metrics.yoymaintenancecost");
			depreciation=jsonpath.get("Car["+CarCount+"].metrics.depreciation");
			listProfit.add(listRevenueCars.get(CarCount)-(yoyCost+depreciation));
		}
		int ProfitIndex=listProfit.indexOf(Collections.max(listProfit));
		System.out.println("The highest Profit producing car is : "+ jsonpath.getString("Car["+ProfitIndex+"].make") + " Car (Color-"+jsonpath.getString("Car["+ProfitIndex+"].metadata.Color")+" Vin#-"+jsonpath.getString("Car["+ProfitIndex+"].vin")+")");
		System.out.println("The highest Profit producing car details are as below : "+jsonpath.get("Car["+ProfitIndex+"]")+"\n");
	}

}
