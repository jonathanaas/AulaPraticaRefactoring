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

    public double getCharge() {
      double result = 0;
      switch (getMovie().getPriceCode()) {
        case Movie.REGULAR:
           result += 2;
           if (getDaysRented() > 2)
              result += (getDaysRented() - 2) * 1.5;
           break;
        case Movie.NEW_RELEASE:
           result += getDaysRented() * 3;
           break;
        case Movie.CHILDRENS:
           result += 1.5;
           if (getDaysRented() > 3)
              result += (getDaysRented() - 3) * 1.5;
           break;
     }
     return result;
   }

   int getFrequentRenterPoints() {
       if ((getMovie().getPriceCode() == Movie.NEW_RELEASE) && getDaysRented() > 1)
          return 2;
       else
          return 1;
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

   private double amountFor(Rental aRental) {
      return aRental.getCharge();
   }

   private double getTotalCharge() {
      double result = 0;
      Enumeration rentals = _rentals.elements();
      while (rentals.hasMoreElements()) {
         Rental each = (Rental) rentals.nextElement();
         result += each.getCharge();
      }
      return result;
   }
  
   private int getTotalFrequentRenterPoints(){
      int result = 0;
      Enumeration rentals = _rentals.elements();
      while (rentals.hasMoreElements()) {
         Rental each = (Rental) rentals.nextElement();
         result += each.getFrequentRenterPoints();
      }
      return result;
   }

   public String statement() {
      Enumeration rentals = _rentals.elements();
      String result = "Rental Record for " + getName() + "\n";
      while (rentals.hasMoreElements()) {
         Rental each = (Rental) rentals.nextElement();

         // show figures for this rental
         result += "\t" + each.getMovie().getTitle()+ "\t" +
                   String.valueOf(each.getCharge()) + "\n";
      }

      // add footer lines
      result +=  "Amount owed is " + String.valueOf(getTotalCharge()) + "\n";
      result += "You earned " + String.valueOf(getTotalFrequentRenterPoints()) +
                      " frequent renter points";
      return result;
   }

   public String htmlStatement() {
      Enumeration rentals = _rentals.elements();
      String result = "<H1>Rentals for <EM>" + getName() + "</EM></H1><P>\n";
      while (rentals.hasMoreElements()) {
         Rental each = (Rental) rentals.nextElement();
         // show figures for each rental
         result += each.getMovie().getTitle()+ ": " +
                  String.valueOf(each.getCharge()) + "<BR>\n";
      }
     
      // add footer lines
      result +=  "<P>You owe <EM>" + String.valueOf(getTotalCharge()) + "</EM><P>\n";
      result += "On this rental you earned <EM>" +
             String.valueOf(getTotalFrequentRenterPoints()) +
             "</EM> frequent renter points<P>";
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