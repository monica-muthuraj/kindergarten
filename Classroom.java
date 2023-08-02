package kindergarten;
/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given seat), and
 * - a Student array parallel to seatingAvailability to show students filed into seats 
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in studentsSitting[i][j])
 * 
 */
public class Classroom {
    private SNode studentsInLine;             // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs;              // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingAvailability;  // represents the classroom seats that are available to students
    private Student[][] studentsSitting;      // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
		studentsInLine      = l;
        musicalChairs       = m;
		seatingAvailability = a;
        studentsSitting     = s;
	}
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students coming into the classroom and standing in line.
     * 
     * Reads students from input file and inserts these students in alphabetical 
     * order to studentsInLine singly linked list.
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the file, say x
     * 2) x lines containing one student per line. Each line has the following student 
     * information separated by spaces: FirstName LastName Height
     * 
     * @param filename the student information input file
     */
    public void makeClassroom ( String filename ) {
        StdIn.setFile(filename);
        int numStudents = StdIn.readInt();

        // array of all students
        String tempFirstName, tempLastName;
        int tempHeight;
        Student[] studentArray = new Student[numStudents];
        for(int a=0; a<numStudents; a++){
            tempFirstName = StdIn.readString();
            tempLastName = StdIn.readString();
            tempHeight = StdIn.readInt();
            Student tempStudent = new Student(tempFirstName, tempLastName, tempHeight);
            studentArray[a] = tempStudent;
        }

        // sort array of students
        for(int b=1; b<studentArray.length; b++){
            Student currentstudent = studentArray[b];
            int c=b-1;
            while(c>=0 && studentArray[c].compareNameTo(currentstudent)<0){
                studentArray[c+1]=studentArray[c];
                c=c-1;
            }
            studentArray[c+1] = currentstudent;
        }
 
        // insert students from array into LL
        studentsInLine = new SNode(studentArray[0], null);
        for(int d=1; d<studentArray.length; d++){
            SNode studentNode = new SNode(studentArray[d], studentsInLine);
            studentsInLine = studentNode;
        }
    }

    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of 
     * available seats inside the classroom. Imagine that unavailable seats are broken and cannot be used.
     * 
     * Reads seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true denotes an available seat)
     *  
     * This method also creates the studentsSitting array with the same number of
     * rows and columns as the seatingAvailability array
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {
        StdIn.setFile(seatingChart);
        int r = StdIn.readInt();
        int c = StdIn.readInt();
        seatingAvailability = new boolean[r][c];
        studentsSitting = new Student[r][c];
        
        // reads file values into seatingAvailability array
        for (int e=0; e<seatingAvailability.length; e++){
            for(int f=0; f<seatingAvailability[0].length; f++){
                seatingAvailability[e][f] = StdIn.readBoolean();
            }
        }
    }

    /**
     * 
     * This method simulates students taking their seats in the classroom.
     * 
     * 1. seats any remaining students from the musicalChairs starting from the front of the list
     * 2. starting from the front of the studentsInLine singly linked list
     * 3. removes one student at a time from the list and inserts them into studentsSitting according to
     *    seatingAvailability
     * 
     * studentsInLine will then be empty
     */
    public void seatStudents () {
        if(musicalChairs!=null){
            for (int g=0; g<seatingAvailability.length; g++){
                for(int h=0; h<seatingAvailability[0].length; h++){
                    if(seatingAvailability[g][h]==true){
                        if(musicalChairs!=null){
                            studentsSitting[g][h]=musicalChairs.getStudent();
                            musicalChairs=null;
                            seatingAvailability[g][h]=false;
                        }
                        else if(studentsInLine!=null){
                            studentsSitting[g][h]=studentsInLine.getStudent();
                            studentsInLine=studentsInLine.getNext();
                            seatingAvailability[g][h]=false;
                        }
                    }
                }
            } 
        }
        else{
            for (int g=0; g<seatingAvailability.length; g++){
                for(int h=0; h<seatingAvailability[0].length; h++){
                    if(seatingAvailability[g][h]==true){
                        if(studentsInLine!=null){
                            studentsSitting[g][h]=studentsInLine.getStudent();
                            studentsInLine=studentsInLine.getNext();
                            seatingAvailability[g][h]=false;
                        }
                    }
                }
            }  
        }
    }

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then moves
     * into second row.
     */
    public void insertMusicalChairs () {
        SNode front = null;
        for (int i=0; i<studentsSitting.length; i++){
            for(int j=0; j<studentsSitting[0].length; j++){
                if(studentsSitting[i][j]!=null){
                    if(musicalChairs==null){
                        musicalChairs = new SNode(studentsSitting[i][j], musicalChairs);
                        front = musicalChairs;
                        studentsSitting[i][j]=null;
                        seatingAvailability[i][j]=true;
                    }
                    else{
                        SNode oldLast = musicalChairs;
                        musicalChairs = new SNode(studentsSitting[i][j], front);
                        oldLast.setNext(musicalChairs);
                        studentsSitting[i][j]=null;
                        seatingAvailability[i][j]=true;
                    }
                }
            }
        }  
     }

    /**
     * 
     * This method repeatedly removes students from the musicalChairs until there is only one
     * student (the winner).
     * 
     * Choose a student to be elimnated from the musicalChairs using StdRandom.uniform(int b),
     * where b is the number of students in the musicalChairs. 0 is the first student in the 
     * list, b-1 is the last.
     * 
     * Removes eliminated student from the list and inserts students back in studentsInLine 
     * in ascending height order (shortest to tallest).
     * 
     * The last line of this method calls the seatStudents() method so that students can be seated.
     */
    public void playMusicalChairs() {
        SNode ptr = musicalChairs.getNext();
        int numStudents= 1;

        // amount of students in musical chairs
        while(ptr!=musicalChairs){
            numStudents++;
            ptr=ptr.getNext();
        }

        // System.out.println("Number of students: " + numStudents);

        Student[] studentsLine = new Student[numStudents-1]; // -1 numstudents
        Student studentToRemove;
        SNode prev = null;
        int count = numStudents;

        // playing musical chairs 
        for(int t=0; t<count-1; t++){
            int numToDelete = StdRandom.uniform(numStudents);
            // System.out.println("Student to delete: " + (numToDelete+1));
            SNode front = musicalChairs.getNext();
            ptr=front;
            for(int s=0; s<numToDelete; s++){
                // System.out.println("Ptr is pointing at: " + ptr.getStudent().getFullName());
                prev=ptr;
                ptr=ptr.getNext();
            }
            studentToRemove=ptr.getStudent();
            // System.out.println("Student to remove: " + ptr.getStudent().getFullName());
            studentsLine[t]=studentToRemove;
            if(ptr==front){
                prev=musicalChairs;
                prev.setNext(ptr.getNext());
                ptr=null;
                prev=null;
                front=musicalChairs.getNext();
            }
            else if(ptr==musicalChairs){
                prev.setNext(ptr.getNext());
                musicalChairs=prev;
                ptr=null;
            }
            else{
                prev.setNext(ptr.getNext());
                ptr=musicalChairs.getNext();
                prev=null;
            }
            numStudents--;
        }

        // sort array of students by height
        for(int y=0; y<studentsLine.length; y++){
            int z=y-1;
            Student checkStudent = studentsLine[y];
            while(z>=0 && studentsLine[z].getHeight() < checkStudent.getHeight()){
                studentsLine[z+1] = studentsLine[z];
                z=z-1;
            }
            studentsLine[z+1] = checkStudent;
        }

        // insert students into studentsInLine LL
        studentsInLine = new SNode(studentsLine[0], null);
        for(int x=1; x<studentsLine.length; x++){
            SNode studentNode = new SNode(studentsLine[x], studentsInLine);
            studentsInLine = studentNode;
        }
        seatStudents();
    } 

    /**
     * Insert a student to wherever the students are at (ie. whatever activity is not empty)
     * Note: adds to the end of either linked list or the next available empty seat
     * @param firstName the first name
     * @param lastName the last name
     * @param height the height of the student
     */
    public void addLateStudent ( String firstName, String lastName, int height ) {
        Student lateStudent = new Student(firstName, lastName, height);
        if(studentsInLine!=null){
            SNode ptr = studentsInLine;
            while(ptr!=null && ptr.getNext()!=null){
                ptr=ptr.getNext();
            }
            SNode lateSNode = new SNode(lateStudent, null);
            ptr.setNext(lateSNode);
        }
        else if(musicalChairs!=null){
            SNode oldLast = musicalChairs;
            musicalChairs = new SNode(lateStudent, musicalChairs.getNext());
            oldLast.setNext(musicalChairs);
        }
        else{
            for (int m=0; m<seatingAvailability.length; m++){
                for(int n=0; n<seatingAvailability[0].length; n++){
                    if(seatingAvailability[m][n]==true && lateStudent!=null){
                        studentsSitting[m][n]=lateStudent;
                        seatingAvailability[m][n]=false;
                        lateStudent=null;
                    }
                }
            } 
        }
    }

    /**
     * A student decides to leave early
     * This method deletes an early-leaving student from wherever the students 
     * are at (ie. whatever activity is not empty)
     * 
     * Assume the student's name is unique
     * 
     * @param firstName the student's first name
     * @param lastName the student's last name
     */
    public void deleteLeavingStudent ( String firstName, String lastName ) {
        if(studentsInLine!=null){
            SNode ptr = studentsInLine;
            SNode prev = null;

            while(ptr!=null){
                if(ptr.getStudent().getFirstName().equalsIgnoreCase(firstName)){
                    if(ptr.getStudent().getLastName().equalsIgnoreCase(lastName)){
                        if(ptr==studentsInLine){
                            studentsInLine=studentsInLine.getNext();
                        }
                        else{
                            prev.setNext(ptr.getNext());
                        }
                    }
                }
                prev=ptr;
                ptr=ptr.getNext();
            }
        }
        else if(musicalChairs!=null){
            SNode ptr = musicalChairs.getNext();
            SNode prev = musicalChairs;

            while(ptr!=musicalChairs){
                if(ptr.getStudent().getFirstName().equalsIgnoreCase(firstName)){
                    if(ptr.getStudent().getLastName().equalsIgnoreCase(lastName)){
                        if(ptr!=musicalChairs){
                            prev.setNext(ptr.getNext());
                            ptr=musicalChairs;
                        }
                    }
                }
                else{
                    prev=ptr;
                    ptr=ptr.getNext();
                    System.out.println(ptr.getStudent().getFullName());
                }

            }
            if(ptr==musicalChairs){
                if(ptr.getStudent().getFirstName().equalsIgnoreCase(firstName)){
                    if(ptr.getStudent().getLastName().equalsIgnoreCase(lastName)){
                        prev.setNext(ptr.getNext());
                        musicalChairs=prev;
                        ptr=null;
                    }
                }
            }
        }
        else{
            for(int p=0; p<seatingAvailability.length; p++){
                for(int q=0; q<seatingAvailability[0].length; q++){
                    if(studentsSitting[p][q]!=null){
                        Student checkStudent = studentsSitting[p][q];
                        if(checkStudent.getFirstName().equalsIgnoreCase(firstName)){
                            if(checkStudent.getLastName().equalsIgnoreCase(lastName)){
                                studentsSitting[p][q]=null;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {
        
            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingAvailability[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else { stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );
                    
                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingAvailability() { return seatingAvailability; }
    public void setSeatingAvailability(boolean[][] a) { seatingAvailability = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

}
