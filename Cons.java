/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          02 Oct 09; 12 Feb 10; 04 Oct 12; 03 Oct 14; 25 Feb 15
 */
import java.util.*;
interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }



@SuppressWarnings("unchecked")
public class Cons
{
    // instance variables
    private Object car;
    private Cons cdr;
    private Cons(Object first, Cons rest)
       { car = first;
         cdr = rest; }
    public static Cons cons(Object first, Cons rest)
      { return new Cons(first, rest); }
    public static boolean consp (Object x)
       { return ( (x != null) && (x instanceof Cons) ); }
// safe car, returns null if lst is null
    public static Object first(Cons lst) {
        return ( (lst == null) ? null : lst.car  ); }
// safe cdr, returns null if lst is null
    public static Cons rest(Cons lst) {
      return ( (lst == null) ? null : lst.cdr  ); }
    public static Object second (Cons x) { return first(rest(x)); }
    public static Object third (Cons x) { return first(rest(rest(x))); }
    public static void setfirst (Cons x, Object i) { x.car = i; }
    public static void setrest  (Cons x, Cons y) { x.cdr = y; }
   public static Cons list(Object ... elements) {
       Cons list = null;
       for (int i = elements.length-1; i >= 0; i--) {
           list = cons(elements[i], list);
       }
       return list;
   }

    // convert a list to a string for printing
    public String toString() {
       return ( "(" + toStringb(this) ); }
    public static String toString(Cons lst) {
       return ( "(" + toStringb(lst) ); }
    private static String toStringb(Cons lst) {
       return ( (lst == null) ?  ")"
                : ( first(lst) == null ? "()" : first(lst).toString() )
                  + ((rest(lst) == null) ? ")" 
                     : " " + toStringb(rest(lst)) ) ); }

    public static int square(int x) { return x*x; }

    // iterative destructive merge using compareTo
public static Cons dmerj (Cons x, Cons y) {
  if ( x == null ) return y;
   else if ( y == null ) return x;
   else { Cons front = x;
          if ( ((Comparable) first(x)).compareTo(first(y)) < 0)
             x = rest(x);
            else { front = y;
                   y = rest(y); };
          Cons end = front;
          while ( x != null )
            { if ( y == null ||
                   ((Comparable) first(x)).compareTo(first(y)) < 0)
                 { setrest(end, x);
                   x = rest(x); }
               else { setrest(end, y);
                      y = rest(y); };
              end = rest(end); }
          setrest(end, y);
          return front; } }

public static Cons midpoint (Cons lst) {
  Cons current = lst;
  Cons prev = current;
  while ( lst != null && rest(lst) != null) {
    lst = rest(rest(lst));
    prev = current;
    current = rest(current); };
  return prev; }

    // Destructive merge sort of a linked list, Ascending order.
    // Assumes that each list element implements the Comparable interface.
    // This function will rearrange the order (but not location)
    // of list elements.  Therefore, you must save the result of
    // this function as the pointer to the new head of the list, e.g.
    //    mylist = llmergesort(mylist);
public static Cons llmergesort (Cons lst) {
  if ( lst == null || rest(lst) == null)
     return lst;
   else { Cons mid = midpoint(lst);
          Cons half = rest(mid);
          setrest(mid, null);
          return dmerj( llmergesort(lst),
                        llmergesort(half)); } }


    // ****** your code starts here ******
    // add other functions as you wish.

public static Cons union (Cons x, Cons y) {
   x=llmergesort(x);
   y=llmergesort(y);
   return mergeunion(x,y);
}

    // following is a helper function for union
public static Cons mergeunion (Cons x, Cons y) {
     if(x==null){
    return y;
    }
    if(y==null){
    return x;
    }
    else if(((Comparable)first(x)).compareTo(first(y))>0){
        return cons(first(y), mergeunion(x,rest(y)));
    }else if(((Comparable)first(x)).compareTo(first(y))<0){
        return cons(first(x), mergeunion(rest(x),y));
    }else{
    return cons(first(x),mergeunion(rest(x),rest(y)));
    }
}

public static Cons setDifference (Cons x, Cons y) {
    x=llmergesort(x);
    y=llmergesort(y);
    return mergediff(x,y);
}

    // following is a helper function for setDifference
public static Cons mergediff (Cons x, Cons y) {
    if(x==null){
    return x;
    }
    if(y==null){
    return x;
    }else if(((Comparable)first(x)).compareTo(first(y)) !=0){
        return cons(first(x), mergediff(rest(x),y));
    }else{
        return mergediff(rest(x),rest(y));
    }
}

public static Cons bank(Cons accounts, Cons updates) {
    Cons accountSafe=accounts;
    updates = llmergesort(updates);
    Cons updatesSafe=updates;
    
    int size = sizeGet(accounts);
    String[] accountNames = new String[size];
    int[] accountBalances = new int[size];
    
    for(int i=0; i<size;i++){
    Account account = (Account)first(accounts);
    accountNames[i] = account.name();
    accountBalances[i] = account.amount();
    accounts=rest(accounts);
    }
    
    size = sizeGet(updates);
    String[] updateNames = new String[size];
    int[] updateBalances = new int[size];
    
    for(int i=0; i <size; i++){
    Account update = (Account)first(updates);
    updateNames[i]=update.name();
    updateBalances[i]= update.amount();
    updates=rest(updates);
    }
    for(int i =0; i<size;i++){
        if(i+1<size && updateNames[i]==updateNames[i+1]){
            if(updateBalances[i]<0){
                if(updateBalances[i+1]>0){
                updateBalances = swapInt(updateBalances,i,i+1);
                i=-1;
                }else{
                    if(updateBalances[i]>updateBalances[i+1]){
                                    updateBalances = swapInt(updateBalances,i,i+1);
                                    i=-1;
                    }
                }
            }
        }
    }
  
    String name = "";
    int balanced = 0;
    for(int i=0;i<accountNames.length;i++){
    name = accountNames[i];
        for(int j=0;j<updateNames.length;j++){
        if(name==updateNames[j]){
        accountBalances[i] += updateBalances[j];
        updateBalances[j] = 0;
        updateNames[j] = null;
            if(accountBalances[i] <0){
            accountBalances[i]-=30;
            System.out.println(name + " account overdrafted. A $30 overdraft fee will apply. The new balance is: $" + accountBalances[i]);
            } 
        }
        
        }
        
    }

        ArrayList<Integer> positions = new ArrayList<Integer>();
    for(int i=0; i<updateNames.length;i++){
    if(updateNames[i]!=null){
    positions.add(i);
    } 
    }
      
    ArrayList<String> additionalNames = new ArrayList<String>();
    ArrayList<Integer> additionalBalances = new ArrayList<Integer>();
    int updatePosition = 0;
    for(int i=0; i<positions.size(); i++){
        if(!additionalNames.contains(updateNames[positions.get(i)])){
        updatePosition = positions.get(i);
        additionalNames.add(updateNames[updatePosition]);
        additionalBalances.add(updateBalances[updatePosition]);
        }else{
        additionalBalances.add(updateBalances[positions.get(i)]);
        
        }
    }
    int sizer=additionalNames.size();
    for(int i=0;i<sizer;i++){
        if(additionalBalances.get(i)<0){
        System.out.println("There  is no account for "+ additionalNames.get(i)+ " The amount is negative at: $" + additionalBalances.get(i) +" so no account will be created");
        additionalNames.remove(i);
        additionalBalances.remove(i);
        i=-1;
        sizer=additionalNames.size();

        }
    }
    
    
    int finalSize = accountNames.length + additionalNames.size();
    String[] submitNames = new String[finalSize];
    int[] submitBalances = new  int[finalSize];
    
    for(int i=0;i<accountNames.length;i++){
        submitNames[i] = accountNames[i];
        submitBalances[i]=accountBalances[i];

    }
    for(int i=0; i<additionalNames.size();i++){
        submitNames[i+accountNames.length] = additionalNames.get(i);
        submitBalances[i+accountNames.length] = additionalBalances.get(i);
        System.out.println("An account was created for : " + additionalNames.get(i) +" in the amount of: $" + additionalBalances.get(i));
    }
    
    for(int i=0; i<submitNames.length-1;i++){
        String check1 = submitNames[i];
        String check2 = submitNames[i+1];
        if(check1.compareTo(check2)>0){
        submitNames= swapString(submitNames,i,i+1);
        submitBalances=swapInt(submitBalances,i,i+1);
        i=-1;
        }
    
    
    
    }
    
    Cons submit = list();
    for(int i = submitNames.length-1; i>=0; i--){
        submit = cons(new Account(submitNames[i], new Integer(submitBalances[i])),submit);

    }
    
           
    return submit;

    
    
    
    }







public static int sizeGet(Cons i){
    int size=0;
while(i!=null){
Account j = (Account)first(i);
size++;
i=rest(i);
}
return size;
}


public static String [] mergearr(String [] x, String [] y) {
    int xlength=x.length;
    int ylength=y.length;
    int xlim=xlength;
    int ylim=ylength;
    int xcount=0;
    int ycount=0;
    String [] answer = new String[xlength+ylength];
    for(int i=0; i<answer.length;i++){
        if(xcount!=xlength&& ycount!=ylength && x[xcount].compareTo(y[ycount])<0){
            answer[i]=x[xcount];
        xcount++;
          
        }else if(xcount!=xlength&& ycount!=ylength &&x[xcount].compareTo(y[ycount])>0){
            answer[i] = y[ycount];
            ycount++;
        }
        else if(xcount!=xlength&& ycount!=ylength && x[xcount].compareTo(y[ycount])==0){
         answer[i] =x[xcount];
            answer[i+1]=y[ycount];
            xcount++;
            ycount++;
            i++;
        }else if (xcount<xlength){
        answer[i]=x[xcount];
        xcount++;
        }else{
        answer[i] = y[ycount];
        ycount++;
        }
        
        
        
        
    }
    return answer;
}

public static boolean markup(Cons text) {
    ArrayList<String> markList = new ArrayList<String>();
    ArrayList<String> markList2= new ArrayList<String>();
    Cons text2 = text;
    String closer = "";
    int position=0;
    while(text!=null){
        markList.add((String)first(text));
        text=rest(text);
    }
    markList2 = markList;
    String [] grid = new String[2];
    int finish = 0;
    int size = markList.size();
    int odd =0;
    for(int i =0;i<size;i++){
            String moo = markList.get(i);
    if(markList.get(i).equals("") || markList.get(i).charAt(0)!='<'){

    odd++;
    }
    } 
    size = markList.size();
    if((size-odd)%2!=0){
    return false;
    }
    for(int i=0; i<size;i++){
    if(tagCheck(markList.get(i))==1){
        grid[0]=markList.get(i);
        position =i;
    
    }else if(tagCheck(markList.get(i))==2){
        grid[1]=markList.get(i);
        if(markup2(markList.get(i)).equals(grid[0])){
            markList.remove(i);
            markList.remove(position);
            grid[0]=null;
            grid[1]=null;
            i=-1;
            size=markList.size();
            if(size==0){
            return true;
            }
        }else if(!markup2(markList.get(i)).equals(grid[0])){
             
                            System.out.println("Error, tag: " + grid[0] +  " was not closed properly. Correct closing tag is: </"+grid[0].substring(1));
            return false;
        }
    }
    
    
    
    
    
    
    
    
    }
    size = markList.size();
    for(int i =0;i<size;i++){
      
    if(markList.get(i).equals("") || markList.get(i).charAt(0)!='<'){

        markList.remove(i);
            size = markList.size();
            i=-1;
    }
    } 
    
    if(markList.size()==0){
    return true;
}else{
    
    
                System.out.println("Error, tag: " + grid[0] +  " was not closed properly. Correct closing tag is: </"+grid[0].substring(1));
return false;
}
    
    
    
}
public static String markup2(String mark){
String n= mark.substring(2);
String k = "<" + n;
return k;

}
public static int tagCheck(String mark){
if(mark.charAt(0)!='<'){
return 0;
}else if(mark.charAt(1)== '/'){
return 2;
}else{
return 1;
}
}
public static int[] swapInt(int [] x, int position1, int position2){
int temp = x[position1];
x[position1]=x[position2];
x[position2]=temp;
return x;
}
public static String[] swapString(String [] x, int position1, int position2){
String temp = x[position1];
x[position1]=x[position2];
x[position2]=temp;
return x;
}
    // ****** your code ends here ******

    public static void main( String[] args )
      { 
         Cons set1 = list("d", "b", "c", "a");
        Cons set2 = list("f", "d", "b", "g", "h");
        System.out.println("set1 = " + Cons.toString(set1));
        System.out.println("set2 = " + Cons.toString(set2));
        System.out.println("union = " + Cons.toString(union(set1, set2)));

        Cons set3 = list("d", "b", "c", "a");
        Cons set4 = list("f", "d", "b", "g", "h");
        System.out.println("set3 = " + Cons.toString(set3));
        System.out.println("set4 = " + Cons.toString(set4));
        System.out.println("difference = " +
                           Cons.toString(setDifference(set3, set4)));

        Cons accounts = list(
               new Account("Arbiter", new Integer(498)),
               new Account("Flintstone", new Integer(102)),
               new Account("Foonly", new Integer(123)),
               new Account("Kenobi", new Integer(373)),
               new Account("Rubble", new Integer(514)),
               new Account("Tirebiter", new Integer(752)),
               new Account("Vader", new Integer(1024)) );

        Cons updates = list(
               new Account("Foonly", new Integer(100)),
               new Account("Flintstone", new Integer(-10)),
               new Account("Arbiter", new Integer(-600)),
               new Account("Garble", new Integer(-100)),
               new Account("Rabble", new Integer(100)),
               new Account("Flintstone", new Integer(-20)),
               new Account("Foonly", new Integer(10)),
               new Account("Tirebiter", new Integer(-200)),
               new Account("Flintstone", new Integer(10)),
               new Account("Flintstone", new Integer(-120))  );
        System.out.println("accounts = " + accounts.toString());
        System.out.println("updates = " + updates.toString());
        Cons newaccounts = bank(accounts, updates);
        System.out.println("result = " + newaccounts.toString());

        String[] arra = {"a", "big", "dog", "hippo"};
        String[] arrb = {"canary", "cat", "fox", "turtle"};
        String[] resarr = mergearr(arra, arrb);
        for ( int i = 0; i < resarr.length; i++ )
            System.out.println(resarr[i]);

        Cons xmla = list( "<TT>", "foo", "</TT>");
        Cons xmlb = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                          "<TD>", "bar", "</TD>", "</TR>",
                          "<TR>", "<TD>", "fum", "</TD>", "<TD>",
                          "baz", "</TD>", "</TR>", "</TABLE>" );
        Cons xmlc = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                          "<TD>", "bar", "</TD>", "</TR>",
                          "<TR>", "<TD>", "fum", "</TD>", "<TD>",
                          "baz", "</TD>", "</WHAT>", "</TABLE>" );
        Cons xmld = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                          "<TD>", "bar", "</TD>", "", "</TR>",
                          "</TABLE>", "</NOW>" );
        Cons xmle = list( "<THIS>", "<CANT>", "<BE>", "foo", "<RIGHT>" );
        Cons xmlf = list( "<CATALOG>",
                          "<CD>",
                          "<TITLE>", "Empire", "Burlesque", "</TITLE>",
                          "<ARTIST>", "Bob", "Dylan", "</ARTIST>",
                          "<COUNTRY>", "USA", "</COUNTRY>",
                          "<COMPANY>", "Columbia", "</COMPANY>",
                          "<PRICE>", "10.90", "</PRICE>",
                          "<YEAR>", "1985", "</YEAR>",
                          "</CD>",
                          "<CD>",
                          "<TITLE>", "Hide", "your", "heart", "</TITLE>",
                          "<ARTIST>", "Bonnie", "Tyler", "</ARTIST>",
                          "<COUNTRY>", "UK", "</COUNTRY>",
                          "<COMPANY>", "CBS", "Records", "</COMPANY>",
                          "<PRICE>", "9.90", "</PRICE>",
                          "<YEAR>", "1988", "</YEAR>",
                          "</CD>", "</CATALOG>");
        System.out.println("xmla = " + xmla.toString());
        System.out.println("result = " + markup(xmla));
        System.out.println("xmlb = " + xmlb.toString());
        System.out.println("result = " + markup(xmlb));
        System.out.println("xmlc = " + xmlc.toString());
        System.out.println("result = " + markup(xmlc));
        System.out.println("xmld = " + xmld.toString());
        System.out.println("result = " + markup(xmld));
        System.out.println("xmle = " + xmle.toString());
        System.out.println("result = " + markup(xmle));
        System.out.println("xmlf = " + xmlf.toString());
        System.out.println("result = " + markup(xmlf));

      }

}



