/**
Date: 11/29/2021

Course: CSCI 2073 40416

Description: This program keeps track of customer's stock holding. This class keeps track of 
              the value of stock bought and sold by a customer and design their portfolio of stock.
             
On my honor, I have neither given nor received unauthorized help while
completing this assignment.

Name: Prasansha Paudel
CWID: 30120811

*/

/*imports required java classes */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Stocks class stores the values of total stock market and uses the overall
 * portfolio to design the portfolio of a particular customer. 
 * It gets its information from comma separated file and takes the information 
 * to manipulate customer input. 
 */
public class Stocks {

    /*global varibale to store total Amount and profit and loss of customers*/
    private double totalAmount;
    private double totalprofitLoss;

    /*Declares map object */
    private Map<String, String> stockMap1; // this stores symbol as its key and title as its value
    private Map<String, String> stockMap2; // this map stores symbol as its key and sector as its value
    private Map<String, Integer> stockMap3; //this map stores symbol as its key and number of shares as it value
    private Map<String, Double> stockMap4; //this map stores symbol as its key and average share as its value

    /**
     * Initializes map object with hashmap.  
     * Reads the dataFile and passes the values of dataFile as a key
     * and its consecutive value on the hashmap object. 
     * @param dataFile intializes map object with the values of comma separated file. 
     */
    public Stocks(String dataFile)
    {
        /*initializes map object */
        stockMap1 = new HashMap<>();
        stockMap2 = new HashMap<>();
        stockMap3 = new HashMap<>();
        stockMap4 = new HashMap<>();

        /*try block reads the dataFile and assisgn key and values to a each hashMap */
        try{
            Scanner in = new Scanner(new File(dataFile));
            String data = "";

            while(in.hasNextLine())
            {
                data = in.nextLine();
                String[] input = data.split(",");
                stockMap1.put(input[0], input[1]); //assigns stock Symbol as key and stock TItle as its value in map. 
                stockMap2.put(input[0], input[2]); //assigns stock Symbol as key and stock Sector as its value in map. 

                stockMap3.put(input[0], 1); //assigns stock Symbol as key and 1 as its value in map. 
                stockMap4.put(input[0], 0.0); //assigns stock Symbol as key and 0.0 as its value in map. 
            } 
            in.close();
        }

        /*catch to prevent exception */
        catch(FileNotFoundException e)
        {
           System.out.println("File not Found");
        }        
    }

    /**
     * This class reads the user instruction file and executes the stock operations by a customer. 
     * This class uses other private mathod developed in the class to execute the task
     * @param transFile gives the instruction of stock oprrations by user
     * @return result String value of all possible output after file execution. 
     */
    public String transactions(String transFile)
    {
        //declares and initializes string variable
        String result = "";
        try
        {
            /*reads the file object and processes the instruction from file.*/
            Scanner in = new Scanner(new File(transFile));
            int numShare = 0;
            double unitShareprice = 0.0;
            /* reads instructions from the file and manipulates the hashmap until the end of file*/
            while(in.hasNextLine())
            {
                String data2 = in.nextLine();
                String[] input2 = data2.split(" ");    //reads line and splits it into an array to make a comparison. 

                /*compares the input from file to BUY and executes condition*/
                if(input2[0].equals("BUY"))
                {
                    numShare = Integer.parseInt(input2[2]);
                    unitShareprice = Double.parseDouble(input2[3]);
                    result += Buy(input2[1], numShare, unitShareprice); /*gets the number of share and average share price 
                                                                         and calls Buy method. */
                }

                /*compares the input from file to SELL and executes condition*/
                if(input2[0].equals("SELL"))
                {
                    numShare = Integer.parseInt(input2[2]);
                    unitShareprice = Double.parseDouble(input2[3]);
                    result += Sell(input2[1], numShare, unitShareprice);  /*gets the number of share and average share price 
                                                                           and calls Sell method. */
                }

                /*compares the input from file to SECTOR and executes condition*/
                if(input2[0].equals("SECTOR"))
                {
                    String newStr = "";
                    for(int i = 1; i < input2.length; i++)
                        newStr += input2[i] + " "; 
                    result += Sector(newStr.substring(0, newStr.length()-1)); /*gets the file instruction and calls Secor method.*/
                }
 
                /*compares the input from file to SUMMARY and executes condition*/
                if(input2[0].equals("SUMMARY"))
                {
                    result += Summary(); /* calls the Summary method*/
                }
                    
            }

            //closes the file
            in.close();     
        }

        /*catch to prevent exception */
        catch(FileNotFoundException e)
        {
            System.out.println("File not Found");
        }

        //returns the updated String value after execution of all methods.
        return result; 
    }

    /**
     * This method gets the symbol, number of shares and average price of stock bought by a customer. 
     * It also calculates the updated average number of shares and updates the hashmap according. 
     * @param symbol Symbol of the stock company bought by customer
     * @param number number of shares of stock company bought by customer
     * @param unitPrice average price of the stock company bought by customer.
     * @return result statement with updated information of stock numbers and price bought by customer.
     */
    private String Buy(String symbol, int number, double unitPrice)
    {
        //delares variable
        String result = ""; 
        int num;
        int newNum;
        double newAmount;
        double quantity;
        /*executes the condition if the given symbol is in the key of stockMap1 */
        if(stockMap1.containsKey(symbol))
        {
            /*checks if the stock share is already bought by customer and calculates an average price 
              from current and previous purchase of stock */
            if(stockMap3.get(symbol) != 0 && stockMap4.get(symbol) != 0.0)
            {
                num = stockMap3.get(symbol);
                quantity = stockMap4.get(symbol);
                newNum = num + number;
                newAmount = (quantity * num + unitPrice * number) / newNum;
                totalAmount += number * unitPrice;
                stockMap3.put(symbol, newNum);      //updates the key and value of hashmap according to the updated purchase. 
                stockMap4.put(symbol, newAmount);   //updates the key and value of hashmap according to the updated purchase. 
                result = String.format("BOUGHT %d shares of %s (holding %d shares at $%.2f per share)\n",
                         number,stockMap1.get(symbol), newNum, newAmount); 

            }

            /*updates the key and value of hashmap according to customer portfolio.  */
            else
            {
                stockMap3.put(symbol, number);
                stockMap4.put(symbol, unitPrice);
                totalAmount += number * unitPrice;
                result = String.format("BOUGHT %d shares of %s (holding %d shares at $%.2f per share)\n",
                          number,stockMap1.get(symbol), number, unitPrice); 
                
            }
        }
        /*gives the message if the symbol is not a key in stockMap1. */
        else
        {
            result += "INVALID TRANSACTION\n";
        }

        //returns updated string value. 
        return result; 
    }

    /**
     * This method gets the symbol, number of shares and average price of stock sold by a customer. 
     * This method gives invalid transaction if the cutomer tries to sell something they do not own. 
     *@param symbol Symbol of the stock company sold by customer
     * @param number number of shares of stock company sold by customer
     * @param unitPrice average price of the stock company sold by customer.
     * @return result statement with updated information of stock numbers and price sold by customer
     *         It returns invalid statement if the given stock symbol is not available.
     */
    private String Sell(String symbol, int number, double unitPrice)
    {
        //declares and initializes variable.
        String result = "";
        int num = 0;
        double amount = 0.0;
        double profitLoss = 0.00;

        /*executes the condition if the given symbol is in the key of stockMap1 */
        if(stockMap1.containsKey(symbol))
        {
            num = stockMap3.get(symbol);
            amount = stockMap4.get(symbol);

            /*compares the total number of share own by a customer with the amount they are trying to sell */
            if(number <= num)
            {
                totalAmount -= number * unitPrice; 
                profitLoss = (number * unitPrice) - (number * amount);
                stockMap3.put(symbol, (num - number));
                stockMap4.put(symbol, amount);
                totalprofitLoss += profitLoss;
                result = String.format("SOLD %d shares of %s at a profit/loss of $%.2f\n",
                                          number, stockMap1.get(symbol), profitLoss);          
            }

            /*gives the message if the transaction is not possible */
            else
            {
                result = "INVALID TRANSACTION\n";
            }
        }

        /*gives the message if the symbol is not a key in stockMap1. */
        else
        {
            result = "INVALID TRANSACTION\n";
        }
        return result; 
    }

    /**
     * This method calculates the abomt of investment in a particular sector. 
     * It calculates the percentage of stock holdings tfrom the total investment in a particular sector.
     * @param symbol Sector information( value of stockMap2)
     * @return result updated information about the total investment in a particular sector. 
     */
    private String Sector(String symbol)
    {
        double totalSector = 0.00;
        String result = ""; 
        double percentage;
        /*iterates throughout the map to get the each value from stockMap2 and compares it with symbol */
        for(Map.Entry<String, String> it: stockMap2.entrySet())
        {
            
            if(it.getValue().equalsIgnoreCase(symbol))
            {
                int num = stockMap3.get(it.getKey());
                double amount = stockMap4.get(it.getKey());    
                totalSector += (num * amount); // calculates the total investment in particular sector.
            } 
            
        }
        if(totalSector > 0.0)
        {
            percentage = (totalSector/ (totalAmount + totalprofitLoss)) * 100; // calculate the percentage of sector in total investment.
            result += String.format("%s: $%.2f (%.2f pct)\n", symbol, totalSector, percentage);
            return result; 
        }
        return "INVALID TRANSACTION\n"; 
        
    }

    /**
     * This method displays the total investment of a customer in their portfolio.
     * @return result string representation to total amount of investment in stock market
     */
    private String Summary()
    {
        String result = "";
        result = String.format("TOTAL ASSETS: $%.2f, TOTAL PROFIT/LOSS: $%.2f\n", 
                              (totalAmount + totalprofitLoss), (totalprofitLoss));
        return result;
    }
    
    
    
}
