/**
	Program to test the basic functionality of the Stocks class
*/
public class StocksTest
{
   public static void main(String [] args)
	{
		
	   Stocks myPortfolio = new Stocks("stocks.csv");
	  // System.out.println(myPortfolio);
	   System.out.print(myPortfolio.transactions("trans.txt"));
	}
}