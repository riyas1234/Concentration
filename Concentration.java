import java.util.*;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

//represents a Card in a Deck
class Card {
  String suit;
  int rank;
  boolean isFaceUp;
  int x;
  int y;

  /*
   * Fields:
   * this.suit... String
   * this.rank... int
   * this.isFaceUp... boolean
   * this.x... int
   * this.y... int
   * Methods:
   * this.draw(WorldScene, int, int)... Void
   * Methods of fields:
   */

  Card(String suit, int rank, boolean isFaceUp, int x, int y) {
    this.suit = suit;
    this.rank = rank;
    this.isFaceUp = isFaceUp;
    this.x = x;
    this.y = y;
  }

  Card(String suit, int rank, boolean isFaceUp) {
    this.suit = suit;
    this.rank = rank;
    this.isFaceUp = isFaceUp;
  }

  void draw(WorldScene scene, int score, int moves) {

    /*
     * Parameters:
     * scene... WorldScene
     * score... int
     * moves... int
     * Methods of parameters:
     */

    ArrayList<String> values = new ArrayList<String>(Arrays.asList("A"
        + "", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"));
    Color color = Color.BLACK;

    // prints out text on screen
    if (score == 0) {
      scene.placeImageXY(new TextImage("You Win!!" + score, 25, Color.WHITE), 200, 380);
    }
    else {
      scene.placeImageXY(new TextImage("Points:" + score, 25, Color.WHITE), 500, 380);
      scene.placeImageXY(new TextImage("Moves:" + moves, 25, Color.WHITE), 200, 380);
    }
    // if the card is not faceUp, prints the back of the card grey color
    if (!isFaceUp) {
      scene.placeImageXY(new FromFileImage("cardBack.jpg"), x, y);
      //scene.placeImageXY(new RectangleImage(40, 65, "solid", Color.GRAY), x, y);
    }

    // prints out the front of the card
    else {
      scene.placeImageXY(new RectangleImage(40, 65, "solid", Color.WHITE), x, y);
      if (this.suit.equals("♦") || this.suit.equals("♥")) {
        color = Color.RED;
      }
      scene.placeImageXY(new TextImage(this.suit, 15, color), x - 12, y - 20);
      scene.placeImageXY(new TextImage(this.suit, 15, color), x + 7, y + 20);
      scene.placeImageXY(new TextImage(values.get(this.rank - 1), 15, color), x, y);
    }

    // adds outline of card
    scene.placeImageXY(new RectangleImage(40, 65, "outline", Color.WHITE), x, y);

    // prints game info
    scene.placeImageXY(new TextImage("Objective: "
        + "\find pairs of cards with equal value and color "
        + "until the board is empty!", 15, Color.WHITE), 350, 420);
    scene.placeImageXY(new TextImage("Keys: "
        + "Use r for reset", 15, Color.WHITE), 128, 435);
    scene.placeImageXY(new TextImage("Rules: you can only see two cards at "
        + "once so to flip them back over, click"
        + " on another card", 15, Color.WHITE), 380, 450);
  }
}

// represents a deck of cards
class Deck {
  ArrayList<Card> cards;
  int score;
  ArrayList<Card> exposedCards;
  int moves;

  /*
   * Fields:
   * this.cards... ArrayList<Card>
   * this.score... int
   * this.exposedCards... ArrayList<Card>
   * this.int... moves
   * Methods:
   * this.draw(WorldScene)... void
   * this.flipCard(int, int)... void
   * this.faceDown()... void
   * this.checkVals(Card, Card)... boolean
   * this.checkMatch()... void
   * Methods of fields:
   */

  Deck(ArrayList<Card> cards, int score, ArrayList<Card> exposedCards, int moves) {
    this.cards = cards;
    this.score = score;
    this.exposedCards = exposedCards;
    this.moves = moves;
  }

  Deck() {
    // score of how many pairs are left to match
    score = 26;
    // list of cards that are currently face up
    exposedCards = new ArrayList<Card>();
    // starts with 0 moves
    moves = 0;
    // eventually is a deck of ordered 52 cards
    ArrayList<Card> freshDeck = new ArrayList<Card>();
    // eventually is a deck of shuffled cards
    ArrayList<Card> cards = new ArrayList<Card>();
    // the four suits of the cards
    ArrayList<String> suits = new ArrayList<String>(Arrays.asList("♣", "♦", "♥", "♠"));

    // makes the deck of 52 cards, in order 
    for (int i = 0; i < 4; i++) {
      String suit;
      suit = suits.get(i);
      for (int j = 0; j < 13; j++) {
        Card newCard = new Card(suit, j + 1, false);
        freshDeck.add(newCard);        
      }
    }

    // adding coordinates and making shuffled deck
    int x = 0;
    int y = 0;
    for (int i = 0; i < 4; i++) {
      y += 80;
      for (int j = 0; j < 13; j++) {
        x += 70; 
        int random = (int) (Math.random() * (freshDeck.size() - 1));
        cards.add(new Card(freshDeck.get(random).suit, freshDeck.get(random).rank, false, x, y));
        freshDeck.remove(random);
      }
      x = 0;
    }
    this.cards = cards;
  }

  // draws all the cards in the current deck 
  public void draw(WorldScene scene) {

    /*
     * Parameters:
     * scene... WorldScene
     * Methods of Parameters:
     */

    for (Card i : this.cards) {
      i.draw(scene, score, moves);
    }
  }

  // flips the card clicked on 
  public void flipCard(int x, int y) {

    /*
     * Parameters:
     * x... int
     * y... int
     * Methods of parameters:
     */

    // checks if the mouse was clicked on which card
    for (Card j : this.cards) {
      if (j.x >= x - 20 && j.x <= x + 20 
          && j.y >= y - 30 && j.y <= y + 30 && exposedCards.size() <= 2) {
        j.isFaceUp = true;
        // makes sure you can't get points from clicking on the same card twice
        if (!(exposedCards.contains(j))) { 
          exposedCards.add(j);
        }
      }
    }
  }

  //makes everything face down again if there is 2 face up
  public void faceDown() {

    /*
     * Parameters:
     * Methods of parameters:
     */

    if (exposedCards.size() > 2) {
      moves++;
      for (Card i : this.cards) {
        i.isFaceUp = false;
      }
      exposedCards.clear();
    }
  }

  // checks whether two cards are a match
  public boolean checkVals(Card one, Card two) {

    /*
     * Parameters:
     * one... Card
     * two... Card
     * Methods of Parameters:
     * one.draw(WorldScene, int, int)... void
     * two.draw(WorldScene, int, int)... void
     */

    // just checks equal value
    if (one.rank == two.rank) {
      // checks matching color
      if ((one.suit).equals("♣")) {
        return ((two.suit).equals("♠"));
      }
      else if ((one.suit).equals("♠")) {
        return ((two.suit).equals("♣"));
      }
      else if ((one.suit).equals("♦")) {
        return ((two.suit).equals("♥"));
      }
      else if ((one.suit).equals("♥")) {
        return ((two.suit).equals("♦"));
      }
    }
    return false;
  }

  // checks whether the two cards open are a match
  public void checkMatch() {

    /*
     * Parameters:
     * Methods of parameters:
     */

    if (exposedCards.size() == 2 && checkVals(exposedCards.get(0), exposedCards.get(1))) {
      for (int i = 0; i < this.cards.size(); i++) {     
        if (exposedCards.contains(cards.get(i))) {
          cards.remove(i);
          i--;
        }
      }
      score--;
    }
  }
}

class Concentration extends World {
  Deck deck;

  /*
   * Fields:
   * this.deck... Deck
   * this.menu... boolean
   * Methods:
   * this.makeScene()... WorldScene
   * this.onMouseReleased(Posn)... void
   * this.onKeyReleased(String)... void
   * this.resetKey()... void
   * Methods of fields:
   * deck.draw(WorldScene)... void
   * deck.flipCard(int, int)... void
   * deck.faceDown()... void
   * deck.checkVals(Card, Card)... boolean
   * deck.checkMatch()... void
   */

  Concentration(Deck deck) {
    this.deck = deck;
  }

  Concentration() {
    this.deck = new Deck();
  }

  // Draws this WorldScene
  public WorldScene makeScene() {

    /*
     * Parameters:
     * Methods of Parameters:
     */

    WorldScene scene = new WorldScene(1000, 500);
    scene.placeImageXY(new RectangleImage(2000, 1000, "solid", Color.BLACK), 0, 0);
    this.deck.draw(scene);
    return scene;
  }

  // checks the mouse press and flips the card
  public void onMouseReleased(Posn posn) {

    /*
     * Parameters:
     * posn... Posn
     * Methods of parameters:
     */

    deck.flipCard(posn.x, posn.y);
    deck.checkMatch();
    deck.faceDown();
  }

  // checks key presses
  public void onKeyReleased(String key) {

    /*
     * Parameters:
     * key... String
     * Methods of Parameters:
     */

    if (key.equals("r")) {
      resetKey();
    }
  }

  // resets the board
  public void resetKey() {

    /*
     * Parameters:
     * Methods of parameters:
     */

    this.deck = new Deck();
  }
}

class ExamplesGame {
  Concentration game = new Concentration();
  Card card1 = new Card("h", 6, false, 0, 0);
  Card card2 = new Card("s", 10, false, 100, 100);
  Card card3 = new Card("h", 6, true, 0, 0);
  Card card4 = new Card("s", 10, true, 100, 100);
  ArrayList<Card> deck = new ArrayList<Card>(Arrays.asList(card1, card2));
  Deck deck1 = new Deck(deck, 10, new ArrayList<Card>(), 6);


  void testBigBang(Tester t) {
    game.bigBang(1000, 500);
  }

  void testFlipCard(Tester t) {
    t.checkExpect(deck1.exposedCards, new ArrayList<Card>());
    t.checkExpect(card1.isFaceUp, false);
    deck1.flipCard(5, 6);
    t.checkExpect(deck1.exposedCards, new ArrayList<Card>(Arrays.asList(card1)));
    t.checkExpect(card1.isFaceUp, true);
    deck1.flipCard(50, 50);
    t.checkExpect(deck1.exposedCards, new ArrayList<Card>(Arrays.asList(card1)));
    t.checkExpect(card1.isFaceUp, true);
    deck1.flipCard(100, 100);
    t.checkExpect(deck1.exposedCards, new ArrayList<Card>(Arrays.asList(card1, card2)));
    t.checkExpect(card1.isFaceUp, true);
    t.checkExpect(card2.isFaceUp, true);
  }

  void testFaceDown(Tester t) {
    ArrayList<Card> deck2 = new ArrayList<Card>(Arrays.asList(card1, card2, card3, card4));
    ArrayList<Card> exposed = new ArrayList<Card>(Arrays.asList(card3, card4, card2));
    Card card5 = new Card("h", 6, true, 0, 0);
    Deck matchDeck = new Deck (deck2, 6, exposed, 5);
    t.checkExpect(matchDeck.cards.get(3).isFaceUp, true);
    t.checkExpect(matchDeck.cards.get(2).isFaceUp, true);
    t.checkExpect(matchDeck.exposedCards.size(), 3);
    matchDeck.faceDown();
    t.checkExpect(matchDeck.cards.get(3).isFaceUp, false);
    t.checkExpect(matchDeck.cards.get(2).isFaceUp, false);
    t.checkExpect(matchDeck.exposedCards.size(), 0);
    matchDeck.faceDown();
    t.checkExpect(matchDeck.cards.get(3).isFaceUp, false);
    t.checkExpect(matchDeck.cards.get(2).isFaceUp, false);
    t.checkExpect(matchDeck.exposedCards.size(), 0);
    matchDeck.cards.add(card5);
    t.checkExpect(matchDeck.cards.get(4).isFaceUp, true);
    t.checkExpect(matchDeck.cards.get(3).isFaceUp, false);
    t.checkExpect(matchDeck.cards.get(2).isFaceUp, false);
  }

  void testResetKey(Tester t) {
    card2.isFaceUp = true;
    ArrayList<Card> deck3 = new ArrayList<Card>(Arrays.asList(card2, card3));
    Deck deck2 = new Deck(deck3, 10, new ArrayList<Card>(Arrays.asList(card3)), 12);
    Concentration game2 = new Concentration(deck2);
    t.checkExpect(game2.deck.score, 10);
    t.checkExpect(game2.deck.moves, 12);
    t.checkExpect(game2.deck.exposedCards.size(), 1);
    game2.resetKey();
    t.checkExpect(game2.deck.score, 26);
    t.checkExpect(game2.deck.moves, 0);
    t.checkExpect(game2.deck.exposedCards.size(), 0);
    t.checkExpect(game2.deck.cards.get(1).isFaceUp, false);
  }

  void testOnKeyReleased(Tester t) {
    card2.isFaceUp = true;
    ArrayList<Card> deck3 = new ArrayList<Card>(Arrays.asList(card2, card3));
    Deck deck2 = new Deck(deck3, 10, new ArrayList<Card>(Arrays.asList(card3)), 12);
    Concentration game2 = new Concentration(deck2);
    t.checkExpect(game2.deck.score, 10);
    t.checkExpect(game2.deck.moves, 12);
    t.checkExpect(game2.deck.exposedCards.size(), 1);
    game2.onKeyReleased("r");
    t.checkExpect(game2.deck.score, 26);
    t.checkExpect(game2.deck.moves, 0);
    t.checkExpect(game2.deck.exposedCards.size(), 0);
    t.checkExpect(game2.deck.cards.get(1).isFaceUp, false);
  }

  void testCheckMatch(Tester t) {
    Card carda = new Card("♦", 5, true, 50, 50);
    Card cardb = new Card("♥", 5, true, 70, 70);
    Deck decka = new Deck(new ArrayList<Card>
    (Arrays.asList(carda, cardb)), 10, new ArrayList<Card>(Arrays.asList(carda, cardb)), 12);
    t.checkExpect(decka.cards.size(), 2);
    t.checkExpect(decka.score, 10);
    decka.checkMatch();
    t.checkExpect(decka.cards.size(), 0);
    t.checkExpect(decka.score, 9);
    
    Card cardc = new Card("♦", 4, true, 50, 50);
    Card cardd = new Card("♥", 5, true, 70, 70);
    Deck deckb = new Deck(new ArrayList<Card>
    (Arrays.asList(cardc, cardd)), 10, new ArrayList<Card>(Arrays.asList(cardc, cardd)), 12);
    t.checkExpect(deckb.cards.size(), 2);
    t.checkExpect(deckb.score, 10);
    deckb.checkMatch();
    t.checkExpect(deckb.cards.size(), 2);
    t.checkExpect(deckb.score, 10);
    
    Card carde = new Card("♣", 5, true, 50, 50);
    Card cardf = new Card("♥", 5, true, 70, 70);
    Deck deckc = new Deck(new ArrayList<Card>
    (Arrays.asList(carde, cardf)), 10, new ArrayList<Card>(Arrays.asList(carde, cardf)), 12);
    t.checkExpect(deckc.cards.size(), 2);
    t.checkExpect(deckc.score, 10);
    deckc.checkMatch();
    t.checkExpect(deckc.cards.size(), 2);
    t.checkExpect(deckc.score, 10);
  }
}
