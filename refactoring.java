abstract class Price {
   abstract int getPriceCode();

   abstract double getCharge(int daysRented);

   int getFrequentRenterPoints(int daysRented) {
       return 1;
   }
 }
 
class ChildrensPrice extends Price {
   int getPriceCode() {
       return Movie.CHILDRENS;
   }

   double getCharge(int daysRented) {
       double result = 1.5;
       if (daysRented > 3)
          result += (daysRented - 3) * 1.5;
       return result;
   }
}
 
class NewReleasePrice extends Price {
   int getPriceCode() {
       return Movie.NEW_RELEASE;
   }

   double getCharge(int daysRented){
       return daysRented * 3;
   }

   int getFrequentRenterPoints(int daysRented) {
       return (daysRented > 1) ? 2: 1;
   }   
}
 
class RegularPrice extends Price {
   int getPriceCode() {
       return Movie.REGULAR;
   }

   double getCharge(int daysRented) {
       double result = 2;
       if (daysRented > 2)
          result += (daysRented - 2) * 1.5;
       return result;
   }
}

public class Movie {

  public static final int  CHILDRENS = 2;
  public static final int  REGULAR = 0;
  public static final int  NEW_RELEASE = 1;

  private String _title;
  private Price _price;

  public Movie(String title, int priceCode) {
      _title = title;
      setPriceCode(priceCode);
  }

  public int getPriceCode() {
      return _price.getPriceCode();
  }

  public void setPriceCode(int arg) {
     switch (arg) {
         case REGULAR:
            _price = new RegularPrice();
            break;
         case CHILDRENS:
            _price = new ChildrensPrice();
            break;
         case NEW_RELEASE:
            _price = new NewReleasePrice();
            break;
         default:
            throw new IllegalArgumentException("Incorrect Price Code");
      }
  }

  public String getTitle (){
      return _title;
  }

  double getCharge(int daysRented) {
      return _price.getCharge(daysRented);
  }

  int getFrequentRenterPoints(int daysRented) {
      return _price.getFrequentRenterPoints(daysRented);
  }
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
      return _movie.getCharge(_daysRented);
   }

   int getFrequentRenterPoints() {
       return _movie.getFrequentRenterPoints(_daysRented);
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
  Movie movieOne = new Movie("acao", 0);
  Movie movieTwo = new Movie("drama", 1);
  Movie movieThree = new Movie("comedia", 2);

  Rental rentalOne = new Rental(movieOne, 5);
  Rental rentalTwo = new Rental(movieTwo, 10);
  Rental rentalThree = new Rental(movieThree, 7);

  Customer customer = new Customer("Lucas");
  customer.addRental(rentalOne);
  customer.addRental(rentalTwo);
  customer.addRental(rentalThree);

  String expected = "Rental Record for Lucas\n";
  expected += " acao  6.5\n";
  expected += " drama 30.0\n";
  expected += " comedia 7.5\n";
  expected += "Amount owed is 44.0\n";
  expected += "You earned 4 frequent renter points" ;

   assertEquals(expected, customer.statement());
}