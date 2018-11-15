package sample;

import java.io.BufferedReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class class Halstead
{
    final char[] HALF_OPERATOR = {'=', '+', '-', '*', '/', '%','!', '>', '<', '&', '|', '?', '~', '^', ':'};

    final String[] FULL_OPERATOR = {"=", "==", "+", "++", "+=", "-", "--", "-=", "*", "*=", "/", "/=", "%", "%=",
            "!", "!=", ">", ">=", ">>", ">>>", "<", "<=", "<<", "&", "&&", "&=", "|", "||", "|=", "?:", "~", "^", "^="};

    private  char holder = '0';
    private  char opCase = '0';
    private  boolean operating = false;
    int N1;
    int N2;
    int nOne;
    int nTwo;
    ArrayList<String> codeList;
    ArrayList<String> operatorList;
    ArrayList<String> operandList;

    public void wipe()
    {
        holder = '0';
        opCase = '0';
        operating = false;
        N1 = 0;
        N2 = 0;
        nOne = 0;
        nTwo = 0;
        codeList = new ArrayList<>();
        operatorList = new ArrayList<>();
        operandList = new ArrayList<>();
    }

    public void parseOps(BufferedReader readFile)throws Exception
    {
        /*File currentFile = new File("NthPrime.java");
        BufferedReader reader = new BufferedReader(new FileReader(currentFile));*/
        StreamTokenizer streamIt = new StreamTokenizer(readFile);

        streamIt.slashStarComments(true);
        while(streamIt.nextToken() != streamIt.TT_EOF){
            if(streamIt.sval != null || streamIt.ttype == StreamTokenizer.TT_NUMBER) {
                if(holder == '>' && opCase == '>'){
                    codeList.add(">>");
                    reset();
                } else if (holder != '0' && holder != '?') {
                    codeList.add("" + holder);
                    reset();
                }
            }
            if (streamIt.sval != null) {
                codeList.add(streamIt.sval);
                operating = false;
            } else if (streamIt.ttype == StreamTokenizer.TT_NUMBER) {
                codeList.add("" + streamIt.nval);
                operating = false;
            } else {
                if (holder == '>' && opCase == '>') {
                    if((char)streamIt.ttype == '>') {
                        codeList.add(">>>");
                    } else if((char)streamIt.ttype == '=') {
                        codeList.add(">>=");
                    }
                    reset();
                } else if (!operating) {
                    for (char c : HALF_OPERATOR) {
                        if (c != ':') {
                            if (c == (char) streamIt.ttype) {
                                holder = (char) streamIt.ttype;
                                operating = true;
                            }
                        }
                    }
                } else {
                    for (char c : HALF_OPERATOR) {
                        if (c == (char) streamIt.ttype) {
                            opCase = (char) streamIt.ttype;
                        }
                    }
                }
            }
            if(operating && opCase != '0') {
                switch (holder){
                    case '=':
                        if(opCase == '=')
                            codeList.add("==");
                        reset();
                        break;

                    case '+':
                        if(opCase == '+')
                            codeList.add("++");
                        else if(opCase == '=')
                            codeList.add("+=");
                        reset();
                        break;

                    case '-':
                        if(opCase == '-')
                            codeList.add("--");
                        else if(opCase == '=')
                            codeList.add("-=");
                        reset();
                        break;

                    case '*':
                        if(opCase == '=')
                            codeList.add("*=");
                        reset();
                        break;

                    case '/':
                        if(opCase == '=')
                            codeList.add("/=");
                        reset();
                        break;

                    case '%':
                        if(opCase == '=')
                            codeList.add("%=");
                        reset();
                        break;

                    case '!':
                        if(opCase == '=')
                            codeList.add("!=");
                        reset();
                        break;

                    case '>':
                        if(opCase == '=')
                            codeList.add(">=");
                        else if(opCase == '>')
                            break;
                        reset();
                        break;

                    case '<':
                        if(opCase == '=')
                            codeList.add("<=");
                        else if(opCase == '<')
                            codeList.add("<<");
                        reset();
                        break;

                    case '&':
                        if(opCase == '&')
                            codeList.add("&&");
                        else if(opCase == '=')
                            codeList.add("&=");
                        reset();
                        break;

                    case '|':
                        if(opCase == '|')
                            codeList.add("||");
                        else if(opCase == '=')
                            codeList.add("|=");
                        reset();
                        break;

                    case '?':
                        if(opCase == ':')
                            codeList.add("?:");
                        reset();
                        break;

                    case '^':
                        if(opCase == '=')
                            codeList.add("^=");
                        reset();
                        break;

                    default:
                        reset();
                        break;
                }
            }
        }
    }

    public void countN()
    {
        int operandIndex = -1;

        for (int i = 0; i < this.codeList.size(); i++) {
            for (String s : FULL_OPERATOR) {
                if(this.codeList.get(i).equals(s)){
                    if(this.codeList.get(i).equals("~")){                                               //~ is in front of operand
                        operatorList.add(this.codeList.get(i));
                        operandList.add(this.codeList.get(i+1));
                        operandIndex = i+1;
                    } else if(this.codeList.get(i).equals("++") || this.codeList.get(i).equals("--")){  //behind operand
                        operatorList.add(this.codeList.get(i));
                        if(i-1 != operandIndex)
                            operandList.add(this.codeList.get(i-1));
                    } else {                                                                            //operands on either side
                        operatorList.add(this.codeList.get(i));
                        operandList.add(this.codeList.get(i+1));
                        if(i-1 != operandIndex)
                            operandList.add(this.codeList.get(i-1));
                        operandIndex = i+1;
                    }
                }
            }
        }
        N1 = operatorList.size();
        N2 = operandList.size();
        Set<String> uniqueOperator = new HashSet<>(operatorList);
        Set<String> uniqueOperand = new HashSet<>(operandList);
        nOne = uniqueOperator.size();
        nTwo = uniqueOperand.size();
    }

    private void reset(){
        holder = '0';
        opCase = '0';
        operating = false;
    }

    public void computeHalstead()
    {
        /*
        this.programVocab = this.nOne + this.nTwo;
        this.programLength = this.N1 + this.N2;
        this.calcProgLength = this.nOne * (Math.log(this.nOne)/Math.log(2))
                + this.nTwo * (Math.log(this.nTwo)/Math.log(2));
        this.volume = this.programLength * (Math.log(this.programVocab)/Math.log(2));
        this.difficulty = (this.nOne/2)*(this.N2/this.nTwo);
        this.effort = this.difficulty * this.volume;
        this.timeReq = this.effort/18;
        this.bugs = this.volume/3000;
        */
    }
}
