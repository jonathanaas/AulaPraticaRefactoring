public class Movie {

  public static final int  CHILDRENS = 2;
  public static final int  REGULAR = 0;
  public static final int  NEW_RELEASE = 1;

  private String _title;
  private int _priceCode;

  public Movie(String title, int priceCode) {
      _title = title;
      _priceCode = priceCode;
  }

  public int getPriceCode() {
      return _priceCode;
  }

  public void setPriceCode(int arg) {
     _priceCode = arg;
  }

  public String getTitle (){
      return _title;
  };
}

class Rental {
    private Movie _movie;
    private int _daysRented;

    public Rental(Movie movie, int daysRented) {
      _movie = movie;
      _daysRented = daysRented;
    }
    public int getDaysRented() {
      return _daysRented;
    }
    public Movie getMovie() {
      return _movie;
    }
}

class Customer {
   private String _name;
   private Vector<Rental> _rentals = new Vector<Rental>();

   public Customer (String name){
      _name = name;
   };

   public void addRental(Rental arg) {
      _rentals.addElement(arg);
   }
   public String getName (){
      return _name;
   };
  
  public String statement() {
     double totalAmount = 0;
     int frequentRenterPoints = 0;
     Enumeration rentals = _rentals.elements();
     String result = "Rental Record for " + getName() + "\n";
     while (rentals.hasMoreElements()) {
        double thisAmount = 0;
        Rental each = (Rental) rentals.nextElement();

        //determine amounts for each line
        switch (each.getMovie().getPriceCode()) {
           case Movie.REGULAR:
              thisAmount += 2;
              if (each.getDaysRented() > 2)
                 thisAmount += (each.getDaysRented() - 2) * 1.5;
              break;
           case Movie.NEW_RELEASE:
              thisAmount += each.getDaysRented() * 3;
              break;
           case Movie.CHILDRENS:
              thisAmount += 1.5;
              if (each.getDaysRented() > 3)
                 thisAmount += (each.getDaysRented() - 3) * 1.5;
               break;
        }

        // add frequent renter points
        frequentRenterPoints ++;
        // add bonus for a two day new release rental
        if ((each.getMovie().getPriceCode() == Movie.NEW_RELEASE) &&
            each.getDaysRented() > 1) frequentRenterPoints ++;

        //show figures for this rental
        result += "\t" + each.getMovie().getTitle()+ "\t" +
            String.valueOf(thisAmount) + "\n";
        totalAmount += thisAmount;

     }
     //add footer lines
     result +=  "Amount owed is " + String.valueOf(totalAmount) + "\n";
     result += "You earned " + String.valueOf(frequentRenterPoints) +
             " frequent renter points";
     return result;
  }
}

public void testStatement(){
  Movie movieOne = new Movie("acao", 001);
  Movie movieTwo = new Movie("drama", 002);
  Movie movieThree = new Movie("comedia", 003);

  Rental rentalOne = new Rental(movieOne, 5);
  Rental rentalTwo = new Rental(movieTwo, 10);
  Rental rentalThree = new Rental(movieThree, 7);

  Customer customer = new Customer("Lucas");
  customer.addRental(rentalOne);
  customer.addRental(rentalTwo);
  customer.addRental(rentalThree);

  String expected = "Rental Record for Lucas\n";
  expected += " acao  15.0\n";
  expected += " drama 12.0\n";
  expected += " comedia 0.0\n";
  expected += "Amount owed is 27.0\n";
  expected += "You earned 4 frequent renter points" ;

   assertEquals(expected, customer.statement());
}