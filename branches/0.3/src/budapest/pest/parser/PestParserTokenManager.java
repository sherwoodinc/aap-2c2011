/* Generated By:JavaCC: Do not edit this line. PestParserTokenManager.java */
package budapest.pest.parser;
import budapest.pest.ast.params.*;
import budapest.pest.ast.proc.*;
import budapest.pest.ast.exp.*;
import budapest.pest.ast.pred.*;
import budapest.pest.ast.pred.trm.*;
import budapest.pest.ast.stmt.*;
import budapest.pest.ast.Type;
import java.util.*;

/** Token Manager. */
public class PestParserTokenManager implements PestParserConstants
{

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x7ffff00L) != 0L)
         {
            jjmatchedKind = 27;
            return 2;
         }
         if ((active0 & 0x80000000000000L) != 0L)
            return 4;
         return -1;
      case 1:
         if ((active0 & 0x4211000L) != 0L)
            return 2;
         if ((active0 & 0x3deef00L) != 0L)
         {
            if (jjmatchedPos != 1)
            {
               jjmatchedKind = 27;
               jjmatchedPos = 1;
            }
            return 2;
         }
         return -1;
      case 2:
         if ((active0 & 0xd00000L) != 0L)
            return 2;
         if ((active0 & 0x30eef00L) != 0L)
         {
            if (jjmatchedPos != 2)
            {
               jjmatchedKind = 27;
               jjmatchedPos = 2;
            }
            return 2;
         }
         return -1;
      case 3:
         if ((active0 & 0x20a6900L) != 0L)
            return 2;
         if ((active0 & 0x1848600L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 3;
            return 2;
         }
         return -1;
      case 4:
         if ((active0 & 0x48600L) != 0L)
            return 2;
         if ((active0 & 0x1800000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 4;
            return 2;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 33:
         jjmatchedKind = 45;
         return jjMoveStringLiteralDfa1_0(0x800000000000L);
      case 34:
         return jjStopAtPos(0, 56);
      case 38:
         return jjMoveStringLiteralDfa1_0(0x100000000000L);
      case 40:
         return jjStopAtPos(0, 30);
      case 41:
         return jjStopAtPos(0, 31);
      case 42:
         return jjStopAtPos(0, 54);
      case 43:
         return jjStopAtPos(0, 52);
      case 44:
         return jjStopAtPos(0, 35);
      case 45:
         return jjStopAtPos(0, 53);
      case 47:
         return jjStartNfaWithStates_0(0, 55, 4);
      case 58:
         jjmatchedKind = 60;
         return jjMoveStringLiteralDfa1_0(0x3f100000000L);
      case 60:
         jjmatchedKind = 49;
         return jjMoveStringLiteralDfa1_0(0x801040000000000L);
      case 61:
         jjmatchedKind = 46;
         return jjMoveStringLiteralDfa1_0(0x400000000000000L);
      case 62:
         jjmatchedKind = 51;
         return jjMoveStringLiteralDfa1_0(0x4000000000000L);
      case 64:
         return jjMoveStringLiteralDfa1_0(0x2000000000000000L);
      case 100:
         return jjMoveStringLiteralDfa1_0(0x10000L);
      case 101:
         return jjMoveStringLiteralDfa1_0(0x1004000L);
      case 102:
         return jjMoveStringLiteralDfa1_0(0x2c60200L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x201000L);
      case 108:
         return jjMoveStringLiteralDfa1_0(0x80400L);
      case 109:
         return jjMoveStringLiteralDfa1_0(0x100000L);
      case 115:
         return jjMoveStringLiteralDfa1_0(0x800L);
      case 116:
         return jjMoveStringLiteralDfa1_0(0x4002100L);
      case 119:
         return jjMoveStringLiteralDfa1_0(0x8000L);
      case 123:
         return jjStopAtPos(0, 33);
      case 124:
         jjmatchedKind = 57;
         return jjMoveStringLiteralDfa1_0(0x80000000000L);
      case 125:
         return jjStopAtPos(0, 34);
      default :
         return jjMoveNfa_0(1, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 33:
         if ((active0 & 0x2000000000L) != 0L)
         {
            jjmatchedKind = 37;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0x20000000000L);
      case 35:
         if ((active0 & 0x8000000000L) != 0L)
            return jjStopAtPos(1, 39);
         break;
      case 38:
         if ((active0 & 0x100000000000L) != 0L)
            return jjStopAtPos(1, 44);
         break;
      case 42:
         if ((active0 & 0x100000000L) != 0L)
            return jjStopAtPos(1, 32);
         break;
      case 45:
         if ((active0 & 0x40000000000L) != 0L)
            return jjStopAtPos(1, 42);
         break;
      case 61:
         if ((active0 & 0x800000000000L) != 0L)
            return jjStopAtPos(1, 47);
         else if ((active0 & 0x1000000000000L) != 0L)
         {
            jjmatchedKind = 48;
            jjmatchedPos = 1;
         }
         else if ((active0 & 0x4000000000000L) != 0L)
            return jjStopAtPos(1, 50);
         return jjMoveStringLiteralDfa2_0(active0, 0x800000000000000L);
      case 62:
         if ((active0 & 0x400000000000000L) != 0L)
            return jjStopAtPos(1, 58);
         break;
      case 63:
         if ((active0 & 0x1000000000L) != 0L)
         {
            jjmatchedKind = 36;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0x4000000000L);
      case 64:
         return jjMoveStringLiteralDfa2_0(active0, 0x10000000000L);
      case 97:
         return jjMoveStringLiteralDfa2_0(active0, 0x180200L);
      case 102:
         if ((active0 & 0x1000L) != 0L)
            return jjStartNfaWithStates_0(1, 12, 2);
         break;
      case 104:
         return jjMoveStringLiteralDfa2_0(active0, 0xa000L);
      case 105:
         return jjMoveStringLiteralDfa2_0(active0, 0x60000L);
      case 107:
         return jjMoveStringLiteralDfa2_0(active0, 0x800L);
      case 108:
         return jjMoveStringLiteralDfa2_0(active0, 0x4000L);
      case 110:
         if ((active0 & 0x200000L) != 0L)
            return jjStartNfaWithStates_0(1, 21, 2);
         break;
      case 111:
         if ((active0 & 0x10000L) != 0L)
            return jjStartNfaWithStates_0(1, 16, 2);
         else if ((active0 & 0x4000000L) != 0L)
            return jjStartNfaWithStates_0(1, 26, 2);
         return jjMoveStringLiteralDfa2_0(active0, 0xc00400L);
      case 112:
         return jjMoveStringLiteralDfa2_0(active0, 0x2000000000000000L);
      case 114:
         return jjMoveStringLiteralDfa2_0(active0, 0x2000100L);
      case 120:
         return jjMoveStringLiteralDfa2_0(active0, 0x1000000L);
      case 124:
         if ((active0 & 0x80000000000L) != 0L)
            return jjStopAtPos(1, 43);
         break;
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 33:
         if ((active0 & 0x4000000000L) != 0L)
            return jjStopAtPos(2, 38);
         return jjMoveStringLiteralDfa3_0(active0, 0x20000000000L);
      case 62:
         if ((active0 & 0x800000000000000L) != 0L)
            return jjStopAtPos(2, 59);
         break;
      case 64:
         return jjMoveStringLiteralDfa3_0(active0, 0x10000000000L);
      case 99:
         return jjMoveStringLiteralDfa3_0(active0, 0x400L);
      case 101:
         return jjMoveStringLiteralDfa3_0(active0, 0x2000L);
      case 105:
         return jjMoveStringLiteralDfa3_0(active0, 0x1008800L);
      case 108:
         return jjMoveStringLiteralDfa3_0(active0, 0x200L);
      case 110:
         return jjMoveStringLiteralDfa3_0(active0, 0x20000L);
      case 111:
         return jjMoveStringLiteralDfa3_0(active0, 0x2000000L);
      case 112:
         if ((active0 & 0x100000L) != 0L)
            return jjStartNfaWithStates_0(2, 20, 2);
         break;
      case 114:
         if ((active0 & 0x400000L) != 0L)
         {
            jjmatchedKind = 22;
            jjmatchedPos = 2;
         }
         return jjMoveStringLiteralDfa3_0(active0, 0x2000000000840000L);
      case 115:
         return jjMoveStringLiteralDfa3_0(active0, 0x84000L);
      case 117:
         return jjMoveStringLiteralDfa3_0(active0, 0x100L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 33:
         if ((active0 & 0x20000000000L) != 0L)
            return jjStopAtPos(3, 41);
         break;
      case 64:
         if ((active0 & 0x10000000000L) != 0L)
            return jjStopAtPos(3, 40);
         break;
      case 97:
         return jjMoveStringLiteralDfa4_0(active0, 0x800400L);
      case 100:
         if ((active0 & 0x20000L) != 0L)
            return jjStartNfaWithStates_0(3, 17, 2);
         break;
      case 101:
         if ((active0 & 0x100L) != 0L)
            return jjStartNfaWithStates_0(3, 8, 2);
         else if ((active0 & 0x4000L) != 0L)
            return jjStartNfaWithStates_0(3, 14, 2);
         else if ((active0 & 0x2000000000000000L) != 0L)
            return jjStopAtPos(3, 61);
         break;
      case 108:
         return jjMoveStringLiteralDfa4_0(active0, 0x8000L);
      case 109:
         if ((active0 & 0x2000000L) != 0L)
            return jjStartNfaWithStates_0(3, 25, 2);
         break;
      case 110:
         if ((active0 & 0x2000L) != 0L)
            return jjStartNfaWithStates_0(3, 13, 2);
         break;
      case 112:
         if ((active0 & 0x800L) != 0L)
            return jjStartNfaWithStates_0(3, 11, 2);
         break;
      case 115:
         return jjMoveStringLiteralDfa4_0(active0, 0x1040200L);
      case 116:
         if ((active0 & 0x80000L) != 0L)
            return jjStartNfaWithStates_0(3, 19, 2);
         break;
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 101:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(4, 9, 2);
         else if ((active0 & 0x8000L) != 0L)
            return jjStartNfaWithStates_0(4, 15, 2);
         break;
      case 108:
         if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_0(4, 10, 2);
         return jjMoveStringLiteralDfa5_0(active0, 0x800000L);
      case 116:
         if ((active0 & 0x40000L) != 0L)
            return jjStartNfaWithStates_0(4, 18, 2);
         return jjMoveStringLiteralDfa5_0(active0, 0x1000000L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 108:
         if ((active0 & 0x800000L) != 0L)
            return jjStartNfaWithStates_0(5, 23, 2);
         break;
      case 115:
         if ((active0 & 0x1000000L) != 0L)
            return jjStartNfaWithStates_0(5, 24, 2);
         break;
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 15;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 1:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 7)
                        kind = 7;
                     jjCheckNAdd(0);
                  }
                  else if (curChar == 47)
                     jjAddStates(0, 1);
                  break;
               case 4:
                  if (curChar == 42)
                     jjCheckNAddTwoStates(10, 11);
                  else if (curChar == 47)
                     jjCheckNAddStates(2, 4);
                  break;
               case 0:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAdd(0);
                  break;
               case 2:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjstateSet[jjnewStateCnt++] = 2;
                  break;
               case 3:
                  if (curChar == 47)
                     jjAddStates(0, 1);
                  break;
               case 5:
                  if ((0xffffffffffffdbffL & l) != 0L)
                     jjCheckNAddStates(2, 4);
                  break;
               case 6:
                  if ((0x2400L & l) != 0L && kind > 5)
                     kind = 5;
                  break;
               case 7:
                  if (curChar == 10 && kind > 5)
                     kind = 5;
                  break;
               case 8:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 9:
                  if (curChar == 42)
                     jjCheckNAddTwoStates(10, 11);
                  break;
               case 10:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(10, 11);
                  break;
               case 11:
                  if (curChar == 42)
                     jjAddStates(5, 6);
                  break;
               case 12:
                  if ((0xffff7fffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(13, 11);
                  break;
               case 13:
                  if ((0xfffffbffffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(13, 11);
                  break;
               case 14:
                  if (curChar == 47 && kind > 6)
                     kind = 6;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 1:
               case 2:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(2);
                  break;
               case 5:
                  jjAddStates(2, 4);
                  break;
               case 10:
                  jjCheckNAddTwoStates(10, 11);
                  break;
               case 12:
               case 13:
                  jjCheckNAddTwoStates(13, 11);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 5:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(2, 4);
                  break;
               case 10:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddTwoStates(10, 11);
                  break;
               case 12:
               case 13:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddTwoStates(13, 11);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 15 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   4, 9, 5, 6, 8, 12, 14, 
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, "\164\162\165\145", 
"\146\141\154\163\145", "\154\157\143\141\154", "\163\153\151\160", "\151\146", "\164\150\145\156", 
"\145\154\163\145", "\167\150\151\154\145", "\144\157", "\146\151\156\144", 
"\146\151\162\163\164", "\154\141\163\164", "\155\141\160", "\151\156", "\146\157\162", 
"\146\157\162\141\154\154", "\145\170\151\163\164\163", "\146\162\157\155", "\164\157", null, null, null, 
"\50", "\51", "\72\52", "\173", "\175", "\54", "\72\77", "\72\41", "\72\77\41", 
"\72\43", "\72\100\100\100", "\72\41\41\41", "\74\55", "\174\174", "\46\46", "\41", 
"\75", "\41\75", "\74\75", "\74", "\76\75", "\76", "\53", "\55", "\52", "\57", "\42", 
"\174", "\75\76", "\74\75\76", "\72", "\100\160\162\145", };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
static final long[] jjtoToken = {
   0x3fffffffcfffff81L, 
};
static final long[] jjtoSkip = {
   0x7eL, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[15];
private final int[] jjstateSet = new int[30];
protected char curChar;
/** Constructor. */
public PestParserTokenManager(SimpleCharStream stream){
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}

/** Constructor. */
public PestParserTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 15; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

}
