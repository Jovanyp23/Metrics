
interface IMetrics {

    boolean setPath(String path);   // sets the file path to process
                                    // returns true if current path is valid
    boolean isSource();             // returns true if the file is a source file

    // basic counts for any file
    //
    int getLineCount();
    int getWordCount();
    int getCharacterCount();

    // source code line counts
    //
    int getSourceLineCount();
    int getCommentLineCount();

    // Halstead metrics
    //
    int getHalsteadn1();            // number of distinct operands
    int getHalsteadn2();            // number of distinct operators
    int getHalsteadN1();            // number of operands
    int getHalsteadN2();            // number of operators

    int getHalsteadVocabulary();
    int getHalsteadProgramLength();
    int getHalsteadCalculatedProgramLenght();
    int getHalsteadVolume();
    int getHalsteadDifficulty();
    int getHalsteadEffort();
    int getHalsteadTime();
    int getHalsteadBugs();
}