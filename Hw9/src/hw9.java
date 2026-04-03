import java.util.*;// For Arrays.asList bc otherwise its a pain
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// Represents a minefield
class Field extends World {
  ArrayList<ArrayList<Node>> array; // the board
  int numMines; // Num mines
  Random rand; // rand num gen
  int scale; // makes the game bigger or smaller, window-wise
  int state; // to tell game state
  boolean firstClick; // to improve accuracy to actual minesweeper
  int timer; // to improve accuracy to actual minesweeper
  boolean isNodePressed;// Private Question @340, to improve accuracy to actual minesweeper
  boolean isFacePressed;// Private Question @340, to improve accuracy to actual minesweeper

  // For normal Gameplay
  Field(int col, int row, int numMines, int scale) {
    this(col, row, numMines, new Random().nextInt(), scale);
  }

  // To test Random
  Field(int col, int row, int numMines, int seed, int scale) {
    this.array = new ArrayList<ArrayList<Node>>(col);
    this.scale = scale;
    this.makeField(col, row);
    this.numMines = numMines;
    this.rand = new Random(seed);
    this.makeMines(this.numMines, this.rand);
    this.state = 1;
    this.firstClick = true;
  }

  // Makes the scene
  @Override
  public WorldScene makeScene() {
    WorldScene ws = new WorldScene(1, 1);
    this.border(ws);
    int nodesRemain = this.array.size() * this.array.get(0).size();
    int flags = this.numMines;
    for (int i = 0; i < this.array.size(); i++) {
      for (int j = 0; j < this.array.get(i).size(); j++) {
        ws = this.array.get(i).get(j).drawNodes(ws, j, i, this.scale);
        if (this.array.get(i).get(j).flagged) {
          flags = flags - 1;
        }
        if (this.array.get(i).get(j).revealed) {
          nodesRemain = nodesRemain - 1;
        }
        if (nodesRemain == this.numMines && this.state == 1) {
          this.state = 2;
        }
      }
    }
    // How many flags the player has left to place to cover the mines
    this.redNum(ws, flags, this.scale);
    // The timer
    this.redNum(ws, this.timer / 10,
        (this.array.get(0).size() * this.scale + this.scale) - this.scale * 51 / 16);
    if (this.firstClick) {
      this.changeDif(ws);
    }
    // If a node is pressed make a shocked expression
    else if (this.isNodePressed) {
      this.smiley(ws, 2);
    }
    // else if the face is pressed, make a shocked expression
    else if (this.isFacePressed) {
      this.smiley(ws, 5);
    }
    // otherwise make the normal one
    else {
      this.smiley(ws, 1);
    }
    if (this.state == 2) {
      this.smiley(ws, 3);
    }
    else if (this.state == 3) {
      this.smiley(ws, 4);
      for (ArrayList<Node> i : this.array) {
        for (Node j : i) {
          if (j.isMine || j.flagged) {
            j.revealed = true;
          }
        }
      }
    }
    return ws;
  }

  public void changeDif(WorldScene ws) {
    int length = (this.array.get(0).size() * this.scale + (this.scale * 10) / 8);
    int scale = this.scale;
    if (this.array.get(0).size() <= 12) {
      scale = this.scale / 2;
    }
    int px = scale / 8;
    int height = this.scale * 13 / 8;
    // Left
    ws.placeImageXY(new RectangleImage(13 * px, 13 * px, "solid", Color.lightGray.darker()),
        length / 2 - 2 * scale, height);
    ws.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(0, 12 * px), new Posn(12 * px, 0),
        "solid", Color.white), length / 2 - 2 * scale, height);
    ws.placeImageXY(new RectangleImage(10 * px, 10 * px, "solid", Color.lightGray),
        length / 2 - 2 * scale, height);
    ws.placeImageXY(new TextImage("B", scale, FontStyle.BOLD, Color.green), length / 2 - 2 * scale,
        height);

    // Middle
    ws.placeImageXY(new RectangleImage(13 * px, 13 * px, "solid", Color.lightGray.darker()),
        length / 2, height);
    ws.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(0, 12 * px), new Posn(12 * px, 0),
        "solid", Color.white), length / 2, height);
    ws.placeImageXY(new RectangleImage(10 * px, 10 * px, "solid", Color.lightGray), length / 2,
        height);
    ws.placeImageXY(new TextImage("I", scale, FontStyle.BOLD, Color.yellow), length / 2, height);

    // Right
    ws.placeImageXY(new RectangleImage(13 * px, 13 * px, "solid", Color.lightGray.darker()),
        length / 2 + 2 * scale, height);
    ws.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(0, 12 * px), new Posn(12 * px, 0),
        "solid", Color.white), length / 2 + 2 * scale, height);
    ws.placeImageXY(new RectangleImage(10 * px, 10 * px, "solid", Color.lightGray),
        length / 2 + 2 * scale, height);
    ws.placeImageXY(new TextImage("E", scale, FontStyle.BOLD, Color.red), length / 2 + 2 * scale,
        height);
  }

  // 13 px tall
  public void smiley(WorldScene ws, int state) {
    int length = (this.array.get(0).size() * this.scale + (this.scale * 10) / 8);
    int px = this.scale / 8;
    ws.placeImageXY(new RectangleImage(13 * px, 13 * px, "solid", Color.lightGray.darker()),
        length / 2, 13 * px);
    ws.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(0, 12 * px), new Posn(12 * px, 0),
        "solid", Color.white), length / 2, 13 * px);
    ws.placeImageXY(new RectangleImage(10 * px, 10 * px, "solid", Color.lightGray), length / 2,
        13 * px);
    ws.placeImageXY(new CircleImage(4 * px + px / 2, "solid", Color.black), length / 2, 13 * px);
    ws.placeImageXY(new CircleImage(4 * px, "solid", Color.yellow), length / 2, 13 * px);
    if (state == 1) {
      ws.placeImageXY(new EllipseImage(4 * px + px / 2, px * 3, "solid", Color.black), length / 2,
          14 * px);
      ws.placeImageXY(new EllipseImage(4 * px, px * 3, "solid", Color.yellow), length / 2,
          13 * px + px / 2);
      ws.placeImageXY(new CircleImage(px * 3 / 4, "solid", Color.black), length / 2 - px - px / 2,
          12 * px);
      ws.placeImageXY(new CircleImage(px * 3 / 4, "solid", Color.black), length / 2 + px + px / 2,
          12 * px);
    }
    // suprise
    else if (state == 2) {
      ws.placeImageXY(new CircleImage(px + px / 4, "solid", Color.black), length / 2, 15 * px);
      ws.placeImageXY(new CircleImage(px * 3 / 4, "solid", Color.yellow), length / 2, 15 * px);
      ws.placeImageXY(new CircleImage(px, "solid", Color.black), length / 2 - px - px / 2, 12 * px);
      ws.placeImageXY(new CircleImage(px, "solid", Color.black), length / 2 + px + px / 2, 12 * px);
    }
    // Sunglasses, smaller smile
    else if (state == 3) {
      ws.placeImageXY(new EllipseImage(4 * px + px / 2, px * 3, "solid", Color.black), length / 2,
          13 * px + px);
      ws.placeImageXY(new EllipseImage(4 * px, px * 3, "solid", Color.yellow), length / 2,
          13 * px + px / 2);
      ws.placeImageXY(new RectangleImage(5 * px, px, "solid", Color.yellow), length / 2, 14 * px);
      ws.placeImageXY(
          new RotateImage(new EllipseImage(2 * px, px + px / 2, "solid", Color.black), 315),
          length / 2 - px, 12 * px);
      ws.placeImageXY(
          new RotateImage(new EllipseImage(2 * px, px + px / 2, "solid", Color.black), 45),
          length / 2 + px, 12 * px);
      ws.placeImageXY(new RectangleImage(4 * px + px, px / 2, "solid", Color.black), length / 2,
          11 * px + px / 2);
      ws.placeImageXY(new CircleImage(px, "solid", Color.black), length / 2 - px - px / 2, 12 * px);
      ws.placeImageXY(new CircleImage(px, "solid", Color.black), length / 2 + px + px / 2, 12 * px);
      ws.placeImageXY(
          new RotateImage(new RectangleImage(2 * px, px / 2, "solid", Color.black), 315),
          length / 2 - 3 * px, 12 * px + px / 4);
      ws.placeImageXY(
          new RotateImage(new RectangleImage(2 * px + px / 2, px / 2, "solid", Color.black), 45),
          length / 2 + 3 * px + px / 2, 12 * px + px / 4);
    }
    // Dead
    else if (state == 4) {
      ws.placeImageXY(
          new CropImage(0, 0, 4 * px + px / 2, px * 5 / 2,
              new EllipseImage(4 * px + px / 2, px * 3, "solid", Color.black)),
          length / 2, 15 * px - px / 4);
      ws.placeImageXY(new CropImage(0, 0, 4 * px, px * 2,
          new EllipseImage(4 * px, px * 3, "solid", Color.yellow)), length / 2, 15 * px);
      ws.placeImageXY(new TextImage("x", this.scale / 2, FontStyle.BOLD, Color.black),
          length / 2 - px - px / 2, 12 * px - px / 2);
      ws.placeImageXY(new TextImage("x", this.scale / 2, FontStyle.BOLD, Color.black),
          length / 2 + px + px / 2, 12 * px - px / 2);
    }
    // Held(gray box)
    else if (state == 5) {
      ws.placeImageXY(new FrameImage(new RectangleImage(12 * px, 12 * px, "solid", Color.lightGray),
          Color.lightGray.darker()), length / 2, 13 * px);
      ws.placeImageXY(new CircleImage(4 * px + px / 2, "solid", Color.black), length / 2 + px / 4,
          13 * px + px / 4);
      ws.placeImageXY(new CircleImage(4 * px, "solid", Color.yellow), length / 2 + px / 4,
          13 * px + px / 4);
      ws.placeImageXY(new EllipseImage(4 * px + px / 2, px * 3, "solid", Color.black),
          length / 2 + px / 4, 14 * px + px / 4);
      ws.placeImageXY(new EllipseImage(4 * px, px * 3, "solid", Color.yellow), length / 2 + px / 4,
          13 * px + px / 2 + px / 4);
      ws.placeImageXY(new CircleImage(px * 3 / 4, "solid", Color.black), length / 2 - px - px / 4,
          12 * px + px / 4);
      ws.placeImageXY(new CircleImage(px * 3 / 4, "solid", Color.black),
          length / 2 + px + px / 2 + px / 4, 12 * px + px / 4);
    }
  }

  // Checks the time
  public void onTick() {
    if (this.state == 1) {
      this.timer = this.timer + 1;
    }
  }

  // Checks when a mouse key is pressed
  // EFFECT: Checks when the left mouse key is held down, and record it
  public void onMousePressed(Posn pos, String button) {
    // Offsets are from the border
    int boxX = (pos.x - (this.scale * 5) / 8) / this.scale;
    int boxY = (pos.y - (3 * this.scale) - this.scale / 4) / this.scale;
    int length = (this.array.get(0).size() * this.scale + (this.scale * 10) / 8);
    int height = (this.array.size() + 2) * this.scale + (this.scale * 15) / 8;
    if (this.state == 1) {
      if ((pos.x >= (this.scale * 5) / 8 && pos.x < length - (this.scale * 5) / 8)
          && (pos.y >= this.scale * 2 + this.scale * 10 / 8 && pos.y < height - this.scale * 5 / 8)
          && (button.equals("LeftButton"))) {
        Node target = this.array.get(boxY).get(boxX);
        if (!target.flagged) {
          this.isNodePressed = true;
          target.hover = true;
        }
      }
    }
    if (((pos.x <= length / 2 + this.scale * 6 / 8 + this.scale / 16
        && pos.x >= length / 2 - this.scale * 6 / 8 - this.scale / 16))
        && ((pos.y <= this.scale + this.scale * 5 / 8 + this.scale * 6 / 8 + this.scale / 16
            && pos.y >= this.scale + this.scale * 5 / 8 - this.scale * 6 / 8 - this.scale / 16))
        && button.equals("LeftButton")) {
      this.isFacePressed = true;
    }
    else {
      this.isFacePressed = false;
    }
  }

  // Checks when a mouse key is released
  // EFFECT: Checks when the left mouse key is released, and record it
  public void onMouseReleased(Posn pos, String button) {
    // Offsets are from the border
    int length = (this.array.get(0).size() * this.scale + (this.scale * 10) / 8);
    int height = (this.array.size() + 2) * this.scale + (this.scale * 15) / 8;

    this.isFacePressed = false;
    this.isNodePressed = false;

    if ((this.state == 1)
        && (pos.x >= (this.scale * 5) / 8 && pos.x < length - (this.scale * 5) / 8)
        && (pos.y >= this.scale * 2 + this.scale * 10 / 8 && pos.y < height - this.scale * 5 / 8)
        && button.equals("LeftButton")) {
      for (int i = 0; i < this.array.size(); i++) {
        for (int j = 0; j < this.array.get(0).size(); j++) {
          Node target = this.array.get(i).get(j);
          if (target.hover) {
            this.onMouseClicked(new Posn((j * this.scale) + (this.scale * 5 / 8),
                (i * this.scale) + (3 * this.scale) + this.scale / 4), button);
            target.hover = false;
          }
        }
      }
    }
  }

  // Handles any clicks that are given
  // EFFECT: switches the states of mines depending on the click given
  public void onMouseClicked(Posn pos, String button) {
    // Offsets are from the border
    int boxX = (pos.x - (this.scale * 5) / 8) / this.scale;
    int boxY = (pos.y - (3 * this.scale) - this.scale / 4) / this.scale;

    int length = (this.array.get(0).size() * this.scale + (this.scale * 10) / 8);
    int height = (this.array.size() + 2) * this.scale + (this.scale * 15) / 8;

    if (this.state == 1) {
      if ((pos.x >= (this.scale * 5) / 8 && pos.x < length - (this.scale * 5) / 8)
          && (pos.y >= this.scale * 2 + this.scale * 10 / 8
              && pos.y < height - this.scale * 5 / 8)) {
        Node target = this.array.get(boxY).get(boxX);
        if (button.equals("RightButton")) {
          if (!target.revealed) {
            target.flagged = !target.flagged;
          }
        }
        else if (button.equals("LeftButton") && !target.flagged) {
          if (!target.revealed) {
            target.revealed = true;
            if (target.isMine && !this.firstClick) {
              target.loseMine = true;
              this.state = 3;
            }
            else {
              // This checks if the first click would be a mine
              if (this.firstClick) {
                while (target.countMines() != 0) {
                  for (Node i : target.neighbors) {
                    for (Node j : i.neighbors) {
                      if (j.isMine) {
                        this.makeMines(1, this.rand);
                        j.isMine = false;
                      }
                    }
                    if (i.isMine) {
                      this.makeMines(1, this.rand);
                      i.isMine = false;
                    }
                  }
                }
                int count = 0;
                for (ArrayList<Node> i : this.array) {
                  for (Node j : i) {
                    if (j.isMine) {
                      count = count + 1;
                    }
                  }
                }
                this.makeMines(this.numMines - count, this.rand);

                if (target.isMine) {
                  for (int i = 0; i < this.array.size(); i++) {
                    for (Node j : this.array.get(i)) {
                      if (!j.isMine) {
                        target.isMine = false;
                        j.isMine = true;
                        i = this.array.size();
                        break;
                      }
                    }
                  }
                }
                this.firstClick = false;

              }

              target.massReveal();
            }
          }
        }
      }
    }

    int scale = this.scale;
    if (this.array.get(0).size() <= 12) {
      scale = this.scale / 2;
    }
    if (this.firstClick) {
      if (((pos.x <= length / 2 + scale * 6 / 8 + scale / 16
          && pos.x >= length / 2 - scale * 6 / 8 - scale / 16))
          && ((pos.y <= this.scale + this.scale * 5 / 8 + scale * 6 / 8 + scale / 16
              && pos.y >= this.scale + this.scale * 5 / 8 - scale * 6 / 8 - scale / 16))) {
        int col = 16;
        int row = 16;
        this.numMines = 40;
        this.array.clear();
        this.timer = 0;
        this.firstClick = true;
        this.state = 1;
        this.makeField(col, row);
        this.makeMines(this.numMines, this.rand);
      }
      else if (((pos.x <= length / 2 + scale * 6 / 8 + scale / 16 - scale * 2
          && pos.x >= length / 2 - scale * 6 / 8 - scale / 16 - scale * 2))
          && ((pos.y <= this.scale + this.scale * 5 / 8 + scale * 6 / 8 + scale / 16
              && pos.y >= this.scale + this.scale * 5 / 8 - scale * 6 / 8 - scale / 16))) {
        int col = 9;
        int row = 9;
        this.numMines = 10;
        this.array.clear();
        this.timer = 0;
        this.firstClick = true;
        this.state = 1;
        this.makeField(col, row);
        this.makeMines(this.numMines, this.rand);
      }
      else if (((pos.x <= length / 2 + scale * 6 / 8 + scale / 16 + scale * 2
          && pos.x >= length / 2 - scale * 6 / 8 - scale / 16 + scale * 2))
          && ((pos.y <= this.scale + this.scale * 5 / 8 + scale * 6 / 8 + scale / 16
              && pos.y >= this.scale + this.scale * 5 / 8 - scale * 6 / 8 - scale / 16))) {
        int col = 30;
        int row = 16;
        this.numMines = 99;
        this.array.clear();
        this.timer = 0;
        this.firstClick = true;
        this.state = 1;
        this.makeField(col, row);
        this.makeMines(this.numMines, rand);
      }
    }
    else if (((pos.x <= length / 2 + this.scale * 6 / 8 + this.scale / 16
        && pos.x >= length / 2 - this.scale * 6 / 8 - this.scale / 16))
        && ((pos.y <= this.scale + this.scale * 5 / 8 + this.scale * 6 / 8 + this.scale / 16
            && pos.y >= this.scale + this.scale * 5 / 8 - this.scale * 6 / 8 - this.scale / 16))
        && !this.firstClick) {
      int col = this.array.get(0).size();
      int row = this.array.size();
      this.array.clear();
      this.timer = 0;
      this.firstClick = true;
      this.state = 1;
      this.makeField(col, row);
      this.makeMines(this.numMines, this.rand);
    }

  }

  // Displays the flags and timer using a 7-segment display
  // EFFECT: adds a 3 digit 7-segement display
  public void redNum(WorldScene ws, int num, int x) {
    int px = (this.scale / 8);
    ws.placeImageXY(new RectangleImage(px * 39 / 2, px * 23 / 2, "solid", Color.black),
        (px * 39 / 4) + x, (px * 23 / 4) + px * 7);
    if (num > 1000) {
      num = 999;
    }
    else if (num < -99) {
      num = -99;
    }
    if (num < 0) {
      digit(ws, -1, x);
      digit(ws, (num % 100) / -10, x + this.scale * 27 / 32);
      digit(ws, (num % 100) % 10 * -1, x + this.scale * 27 / 16);
    }
    else {
      digit(ws, num / 100, x);
      digit(ws, (num % 100) / 10, x + this.scale * 27 / 32);
      digit(ws, (num % 100) % 10, x + this.scale * 27 / 16);
    }
  }

  // makes each digit for the display
  // EFFECT: Makes a digit based on dig
  public void digit(WorldScene ws, int dig, int x) {
    Color on = Color.red;
    Color off = Color.red.darker().darker();
    int miniPx = scale / 16;
    WorldImage trapezoidOn = new BesideImage(
        new TriangleImage(new Posn(0, 0), new Posn(miniPx * 3, 0), new Posn(miniPx * 3, miniPx * 3),
            "solid", on),
        new RectangleImage(miniPx * 3, miniPx * 3, "solid", on), new TriangleImage(new Posn(0, 0),
            new Posn(miniPx * 3, 0), new Posn(0, miniPx * 3), "solid", on));
    WorldImage trapezoidOff = new BesideImage(
        new TriangleImage(new Posn(0, 0), new Posn(miniPx * 3, 0), new Posn(miniPx * 3, miniPx * 3),
            "solid", off),
        new RectangleImage(miniPx * 3, miniPx * 3, "solid", off), new TriangleImage(new Posn(0, 0),
            new Posn(miniPx * 3, 0), new Posn(0, miniPx * 3), "solid", off));
    WorldImage hexagonOn = new BesideImage(
        new TriangleImage(new Posn(0, 0), new Posn(miniPx, miniPx), new Posn(miniPx, miniPx * -1),
            "solid", on),
        new RectangleImage(miniPx * 3, miniPx * 3, "solid", on), new TriangleImage(
            new Posn(miniPx, 0), new Posn(0, miniPx), new Posn(0, miniPx * -1), "solid", on));
    WorldImage hexagonOff = new BesideImage(
        new TriangleImage(new Posn(0, 0), new Posn(miniPx, miniPx), new Posn(miniPx, miniPx * -1),
            "solid", off),
        new RectangleImage(miniPx * 3, miniPx * 3, "solid", off), new TriangleImage(
            new Posn(miniPx, 0), new Posn(0, miniPx), new Posn(0, miniPx * -1), "solid", off));

    if (dig == 0) {
      ws.placeImageXY(trapezoidOn, x + miniPx * 6, this.scale + miniPx);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 270), x + miniPx * 3, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOn, 270), x + miniPx * 3, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOn, 180), x + miniPx * 6, this.scale + miniPx * 18);
      ws.placeImageXY(hexagonOff, x + miniPx * 6, this.scale + miniPx * 19 / 2);
    }
    else if (dig == 1) {
      ws.placeImageXY(trapezoidOff, x + miniPx * 6, this.scale + miniPx);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOff, 270), x + miniPx * 3, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOff, 270), x + miniPx * 3, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOff, 180), x + miniPx * 6, this.scale + miniPx * 18);
      ws.placeImageXY(hexagonOff, x + miniPx * 6, this.scale + miniPx * 19 / 2);
    }
    else if (dig == 2) {
      ws.placeImageXY(trapezoidOn, x + miniPx * 6, this.scale + miniPx);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOff, 270), x + miniPx * 3, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOff, 90), x + miniPx * 9, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOn, 270), x + miniPx * 3, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOn, 180), x + miniPx * 6, this.scale + miniPx * 18);
      ws.placeImageXY(hexagonOn, x + miniPx * 6, this.scale + miniPx * 19 / 2);
    }
    else if (dig == 3) {
      ws.placeImageXY(trapezoidOn, x + miniPx * 6, this.scale + miniPx);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOff, 270), x + miniPx * 3, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOff, 270), x + miniPx * 3, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOn, 180), x + miniPx * 6, this.scale + miniPx * 18);
      ws.placeImageXY(hexagonOn, x + miniPx * 6, this.scale + miniPx * 19 / 2);
    }
    else if (dig == 4) {
      ws.placeImageXY(trapezoidOff, x + miniPx * 6, this.scale + miniPx);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 270), x + miniPx * 3, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOff, 270), x + miniPx * 3, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOff, 180), x + miniPx * 6, this.scale + miniPx * 18);
      ws.placeImageXY(hexagonOn, x + miniPx * 6, this.scale + miniPx * 19 / 2);
    }
    else if (dig == 5) {
      ws.placeImageXY(trapezoidOn, x + miniPx * 6, this.scale + miniPx);
      ws.placeImageXY(new RotateImage(trapezoidOff, 90), x + miniPx * 9, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 270), x + miniPx * 3, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOff, 270), x + miniPx * 3, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOn, 180), x + miniPx * 6, this.scale + miniPx * 18);
      ws.placeImageXY(hexagonOn, x + miniPx * 6, this.scale + miniPx * 19 / 2);
    }
    else if (dig == 6) {
      ws.placeImageXY(trapezoidOn, x + miniPx * 6, this.scale + miniPx);
      ws.placeImageXY(new RotateImage(trapezoidOff, 90), x + miniPx * 9, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 270), x + miniPx * 3, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOn, 270), x + miniPx * 3, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOn, 180), x + miniPx * 6, this.scale + miniPx * 18);
      ws.placeImageXY(hexagonOn, x + miniPx * 6, this.scale + miniPx * 19 / 2);
    }
    else if (dig == 7) {
      ws.placeImageXY(trapezoidOn, x + miniPx * 6, this.scale + miniPx);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOff, 270), x + miniPx * 3, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOff, 270), x + miniPx * 3, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOff, 180), x + miniPx * 6, this.scale + miniPx * 18);
      ws.placeImageXY(hexagonOff, x + miniPx * 6, this.scale + miniPx * 19 / 2);
    }
    else if (dig == 8) {
      ws.placeImageXY(trapezoidOn, x + miniPx * 6, this.scale + miniPx);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 270), x + miniPx * 3, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOn, 270), x + miniPx * 3, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOn, 180), x + miniPx * 6, this.scale + miniPx * 18);
      ws.placeImageXY(hexagonOn, x + miniPx * 6, this.scale + miniPx * 19 / 2);
    }
    else if (dig == 9) {
      ws.placeImageXY(trapezoidOn, x + miniPx * 6, this.scale + miniPx);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 270), x + miniPx * 3, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOn, 90), x + miniPx * 9, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOff, 270), x + miniPx * 3, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOn, 180), x + miniPx * 6, this.scale + miniPx * 18);
      ws.placeImageXY(hexagonOn, x + miniPx * 6, this.scale + miniPx * 19 / 2);
    }
    else if (dig < 0) {
      ws.placeImageXY(trapezoidOff, x + miniPx * 6, this.scale + miniPx);
      ws.placeImageXY(new RotateImage(trapezoidOff, 90), x + miniPx * 9, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOff, 270), x + miniPx * 3, this.scale + miniPx * 5);
      ws.placeImageXY(new RotateImage(trapezoidOff, 90), x + miniPx * 9, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOff, 270), x + miniPx * 3, this.scale + miniPx * 14);
      ws.placeImageXY(new RotateImage(trapezoidOff, 180), x + miniPx * 6, this.scale + miniPx * 18);
      ws.placeImageXY(hexagonOn, x + miniPx * 6, this.scale + miniPx * 19 / 2);
    }
  }

  // Creates the border of the game, using ALOT of shapes.
  // EFFECT: adds a graphical border to the game
  public void border(WorldScene ws) {
    int length = (this.array.get(0).size() * this.scale + (this.scale * 10) / 8);
    int height = (this.array.size() + 2) * this.scale + (this.scale * 15) / 8;
    // left/upper rim
    ws.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(length, 0), new Posn(0, height),
        "solid", Color.white), length / 2, height / 2);
    // right/lower rim
    ws.placeImageXY(new TriangleImage(new Posn(length, height), new Posn(length, 0),
        new Posn(0, height), "solid", Color.lightGray.darker()), length / 2, height / 2);
    // border fix
    ws.placeImageXY(
        new TriangleImage(new Posn(this.scale, this.scale), new Posn(this.scale, 0),
            new Posn(0, this.scale), "solid", Color.lightGray.darker()),
        this.scale / 2, height - this.scale / 2);
    // border
    ws.placeImageXY(new RectangleImage(length - (this.scale / 4), height - this.scale / 4, "solid",
        Color.lightGray), length / 2, height / 2);
    // top panel left/upper rim
    ws.placeImageXY(
        new TriangleImage(new Posn(0, 0),
            new Posn(length - (this.scale * 10 / 8) + this.scale / 4, 0),
            new Posn(0, 2 * this.scale + this.scale / 4), "solid", Color.lightGray.darker()),
        length / 2, (2 * this.scale + (this.scale * 10) / 8) / 2);
    // top panel right/lower rim
    ws.placeImageXY(
        new TriangleImage(
            new Posn(length - (this.scale * 10 / 8) + this.scale / 4,
                2 * this.scale + this.scale / 4),
            new Posn(length - (this.scale * 10 / 8) + this.scale / 4, 0),
            new Posn(0, 2 * this.scale + this.scale / 4), "solid", Color.white),
        length / 2, (2 * this.scale + (this.scale * 10) / 8) / 2);
    // top panel fixes
    ws.placeImageXY(
        new TriangleImage(new Posn(0, 0), new Posn(this.scale * 2, 0), new Posn(0, this.scale * 2),
            "solid", Color.lightGray.darker()),
        length - (this.scale * 3 / 2), (this.scale * 3 / 2));
    ws.placeImageXY(
        new TriangleImage(new Posn(this.scale * 2, this.scale * 2), new Posn(this.scale * 2, 0),
            new Posn(0, this.scale * 2), "solid", Color.white),
        this.scale * 3 / 2, this.scale * 14 / 8);
    // top panel
    ws.placeImageXY(new RectangleImage(length - (this.scale * 10 / 8), 2 * this.scale, "solid",
        Color.lightGray), length / 2, (2 * this.scale + (this.scale * 10) / 8) / 2);
    // bottom panel left/upper rim
    ws.placeImageXY(
        new TriangleImage(new Posn(0, 0),
            new Posn(length - (this.scale * 10 / 8) + this.scale / 4, 0),
            new Posn(0, this.array.size() * this.scale + this.scale / 4), "solid",
            Color.lightGray.darker()),
        length / 2, (height + (2 * this.scale + this.scale * 5 / 8)) / 2);
    // bottom panel right/lower rim
    ws.placeImageXY(
        new TriangleImage(
            new Posn(length - (this.scale * 10 / 8) + this.scale / 4,
                this.array.size() * this.scale + this.scale / 4),
            new Posn(length - (this.scale * 10 / 8) + this.scale / 4, 0),
            new Posn(0, this.array.size() * this.scale + this.scale / 4), "solid", Color.white),
        length / 2, (height + (2 * this.scale + this.scale * 5 / 8)) / 2);
    // bottom panel fixes
    ws.placeImageXY(
        new TriangleImage(new Posn(0, 0), new Posn(this.scale * 2, 0), new Posn(0, this.scale * 2),
            "solid", Color.lightGray.darker()),
        length - (this.scale * 3 / 2),
        (2 * this.scale + (this.scale * 10) / 8) + this.scale * 7 / 8);
    ws.placeImageXY(new TriangleImage(new Posn(this.scale, this.scale), new Posn(this.scale, 0),
        new Posn(0, this.scale), "solid", Color.white), this.scale, height - this.scale);
    // mines background
    ws.placeImageXY(new RectangleImage(length - (this.scale * 10 / 8),
        this.array.size() * this.scale, "solid", Color.white), length / 2,
        (height + (2 * this.scale + this.scale * 5 / 8)) / 2);
  }

  // initialises the nodes
  // Adds col x row nodes to the Arraylist
  public void makeField(int col, int row) {
    for (int i = 0; i < row; i++) {
      ArrayList<Node> rows = new ArrayList<Node>(col);
      for (int j = 0; j < col; j++) {

        if (i != 0 && (j != 0 && j != col - 1)) {
          rows.add(new Node(
              new ArrayList<>(Arrays.asList(rows.get(j - 1), this.array.get(i - 1).get(j - 1),
                  this.array.get(i - 1).get(j), this.array.get(i - 1).get(j + 1)))));
        }
        else if (j == 0 && i != 0) {
          rows.add(new Node(new ArrayList<>(
              Arrays.asList(this.array.get(i - 1).get(j), this.array.get(i - 1).get(j + 1)))));
        }
        else if (i == 0 && j != 0) {
          rows.add(new Node(new ArrayList<>(Arrays.asList(rows.get(j - 1)))));
        }
        else if (j == col - 1 && i != 0) {
          rows.add(new Node(new ArrayList<>(Arrays.asList(rows.get(j - 1),
              this.array.get(i - 1).get(j - 1), this.array.get(i - 1).get(j)))));
        }
        else {
          rows.add(new Node(new ArrayList<>(Arrays.asList())));
        }
      }
      this.array.add(rows);
    }
  }

  // Initialises mines
  // EFFECT: Turns N nodes into mines according to the random seed
  public void makeMines(int mines, Random seed) {
    for (int i = 0; i < mines; i++) {
      int rand1 = seed.nextInt(this.array.size());
      int rand2 = new Random(seed.nextInt()).nextInt(this.array.get(rand1).size());
      Node target = this.array.get(rand1).get(rand2);
      // this makes sure that it gives the proper number of mines
      if (target.isMine) {
        i = i - 1;
      }
      else if (!target.isMine) {
        target.isMine = true;
      }
    }

  }

}

//Represents a node in the game
class Node {
  ArrayList<Node> neighbors;
  boolean isMine;
  boolean revealed;
  boolean flagged;
  boolean hover;
  boolean loseMine;

  Node(ArrayList<Node> list) {
    this.neighbors = list;
    this.revealed = false;
    this.flagged = false;
    this.hover = false;
    this.loseMine = false;
    for (Node i : list) {
      i.neighbors.add(this);
    }
  }

  // Draws the node according to it's state
  WorldScene drawNodes(WorldScene ws, int x, int y, int scale) {
    int posX = (x * scale) + (scale * 1 / 2) + (scale * 5) / 8;
    int posY = ((y + 3) * scale) + ((scale * 3) / 4);
    // Normal space
    if (!this.revealed) {
      if (this.hover) {
        ws.placeImageXY(
            new FrameImage(new RectangleImage(scale - 1, scale - 1, "solid", Color.lightGray),
                Color.lightGray.darker()),
            posX, posY);
      }
      else {
        ws.placeImageXY(new TriangleImage(new Posn(scale, scale), new Posn(0, scale),
            new Posn(scale, 0), "solid", Color.lightGray.darker()), posX, posY);
        ws.placeImageXY(new RectangleImage(scale * 3 / 4, scale * 3 / 4, "solid", Color.lightGray),
            posX, posY);
      }

    }
    // Revealed & not a mine
    else if (!this.isMine && !this.flagged) {
      ws.placeImageXY(
          new FrameImage(new RectangleImage(scale - 1, scale - 1, "solid", Color.lightGray),
              Color.lightGray.darker()),
          posX, posY);
      int mine = this.countMines();
      Color color = new Color(154, 200, 230);
      // The first step i made to commiting to this bit
      switch (mine) {
        case 1:
          color = Color.blue;
          break;
        case 2:
          color = Color.green;
          break;
        case 3:
          color = Color.red;
          break;
        case 4:
          color = Color.blue.darker();
          break;
        case 5:
          color = Color.red.darker();
          break;
        case 6:
          color = Color.cyan;
          break;
        case 7:
          color = Color.black;
          break;
        case 8:
          color = Color.darkGray;
          break;
        default:
          color = new Color(154, 200, 230);
          break;
      }
      if (this.countMines() != 0) {
        ws.placeImageXY(new TextImage(Integer.toString(this.countMines()), scale, color), posX,
            posY);
      }
    }
    // Revealed, flagged, but not a mine(for if you incorrectly flag something)
    else if (this.flagged && this.revealed && !this.isMine) {
      ws.placeImageXY(
          new FrameImage(new RectangleImage((scale - 1), (scale - 1), "solid", Color.lightGray),
              Color.lightGray.darker()),
          posX, posY);
      // Mine
      ws.placeImageXY(new CircleImage(scale * 9 / 32, "solid", Color.black), posX, posY);
      ws.placeImageXY(new LineImage(new Posn(0, scale * 3 / 4), Color.black), posX, posY);
      ws.placeImageXY(new LineImage(new Posn(scale * 3 / 4, 0), Color.black), posX, posY);
      ws.placeImageXY(new LineImage(new Posn(scale * 3 / 4, scale * 3 / 4), Color.black), posX,
          posY);
      ws.placeImageXY(new LineImage(new Posn(-scale * 3 / 4, scale * 3 / 4), Color.black), posX,
          posY);
      ws.placeImageXY(new CircleImage(scale / 8, "solid", Color.white), posX - (scale / 16),
          posY - (scale / 16));
      ws.placeImageXY(
          new RotateImage(
              new ShearedImage(
                  new RectangleImage(scale, scale * 3 / 32, OutlineMode.SOLID, Color.red), -1.5, 0),
              45),
          posX, posY);
      ws.placeImageXY(
          new RotateImage(
              new ShearedImage(
                  new RectangleImage(scale * 3 / 32, scale, OutlineMode.SOLID, Color.red), 0, -1.5),
              45),
          posX, posY);
    }
    // A mine
    else {
      Color color = Color.lightGray;
      if (this.loseMine) {
        color = Color.red;
      }
      ws.placeImageXY(new FrameImage(new RectangleImage((scale - 1), (scale - 1), "solid", color),
          Color.lightGray.darker()), posX, posY);
      // Mine
      ws.placeImageXY(new CircleImage(scale * 9 / 32, "solid", Color.black), posX, posY);
      ws.placeImageXY(new LineImage(new Posn(0, scale * 3 / 4), Color.black), posX, posY);
      ws.placeImageXY(new LineImage(new Posn(scale * 3 / 4, 0), Color.black), posX, posY);
      ws.placeImageXY(new RotateImage(new LineImage(new Posn(scale * 3 / 4, 0), Color.black), 45),
          posX, posY);
      ws.placeImageXY(new RotateImage(new LineImage(new Posn(scale * 3 / 4, 0), Color.black), 315),
          posX, posY);
      ws.placeImageXY(new CircleImage(scale / 8, "solid", Color.white), posX - (scale / 16),
          posY - (scale / 16));
    }
    // Flagged (the lower comment helps for seeing mines easier)
    if (this.flagged && !this.revealed) {
      ws.placeImageXY(new RectangleImage(scale / 16, scale / 2, "solid", Color.black),
          posX + scale / 32, posY);
      ws.placeImageXY(
          new TriangleImage(new Posn(0, 0), new Posn(scale * 3 / 8, scale * 3 / 16),
              new Posn(scale * 3 / 8, scale * 3 / -16), "solid", Color.red),
          posX - scale / 8, posY - scale / 8);
      ws.placeImageXY(new RectangleImage(scale / 4, scale / 16, "solid", Color.black), posX,
          posY + scale * 3 / 16);
      ws.placeImageXY(new RectangleImage(scale / 2, scale / 8, "solid", Color.black), posX,
          posY + scale * 4 / 16);
    }
    return ws;
  }

  // Counts the mines around this node
  int countMines() {
    int mines = 0;
    for (Node i : this.neighbors) {
      if (i.isMine) {
        mines = mines + 1;
      }
    }
    return mines;
  }

  // Reveals this node as well as the surrounding nodes if allowed
  // EFFECT: Reveals this node and if allowed, the surrounding nodes.
  void massReveal() {
    this.revealed = true;
    if (this.countMines() == 0 && !this.isMine) {
      for (Node i : this.neighbors) {
        if (!i.isMine && !i.revealed && !i.flagged) {
          i.revealed = true;
          i.massReveal();
        }
      }
    }
  }
}

// Represents examples and tests for minesweeper
class ExamplesMines {
  WorldScene ws;
  WorldScene ws2;
  Field field;
  Random rand;
  ArrayList<ArrayList<Node>> testList;
  // 1 2 3
  // 4 5 6
  // 7 8 9
  Node node1;
  Node node2;
  Node node3;
  Node node4;
  Node node5;
  Node node6;
  Node node7;
  Node node8;
  Node node9;

  void InitData() {
    this.ws = new WorldScene(1, 1);
    this.ws2 = new WorldScene(1, 1);
    this.field = new Field(30, 16, 99, 10, 32);
    this.rand = new Random(29);
    this.node1 = new Node(new ArrayList<>());
    this.node2 = new Node(new ArrayList<>(Arrays.asList(this.node1)));
    this.node3 = new Node(new ArrayList<>(Arrays.asList(this.node2)));
    this.node4 = new Node(new ArrayList<>(Arrays.asList(this.node1, this.node2)));
    this.node5 = new Node(
        new ArrayList<>(Arrays.asList(this.node1, this.node2, this.node3, this.node4)));
    this.node6 = new Node(new ArrayList<>(Arrays.asList(this.node2, this.node3, this.node5)));
    this.node7 = new Node(new ArrayList<>(Arrays.asList(this.node4, this.node5)));
    this.node8 = new Node(
        new ArrayList<>(Arrays.asList(this.node4, this.node5, this.node6, this.node7)));
    this.node9 = new Node(new ArrayList<>(Arrays.asList(this.node5, this.node6, this.node8)));
    this.testList = new ArrayList<ArrayList<Node>>();
    this.testList.add(new ArrayList<Node>(Arrays.asList(this.node1, this.node2, this.node3)));
    this.testList.add(new ArrayList<Node>(Arrays.asList(this.node4, this.node5, this.node6)));
    this.testList.add(new ArrayList<Node>(Arrays.asList(this.node7, this.node8, this.node9)));
  }

  void testMakeScene(Tester t) {
    this.InitData();
    this.field.array = this.testList;
    this.field.numMines = 1;
    this.field.border(this.ws2);
    this.field.redNum(this.ws2, 1, this.field.scale);
    this.field.redNum(this.ws2, 0,
        (3 * this.field.scale + this.field.scale) - this.field.scale * 51 / 16);
    this.field.changeDif(this.ws2);
    t.checkExpect(this.field.makeScene(), this.ws2);

    this.InitData();
    this.field.array = this.testList;
    this.field.numMines = 5;
    this.field.border(this.ws2);
    // change numMines and the flags youre allotted should also increase
    this.field.redNum(this.ws2, 5, this.field.scale);
    this.field.redNum(this.ws2, 0,
        (3 * this.field.scale + this.field.scale) - this.field.scale * 51 / 16);
    this.field.firstClick = false;
    this.field.smiley(this.ws2, 1);
    t.checkExpect(this.field.makeScene(), this.ws2);

    this.InitData();
    this.field.array = this.testList;
    this.field.numMines = 5;
    this.field.border(this.ws2);
    // change numMines and the flags youre allotted should also increase
    this.field.redNum(this.ws2, 5, this.field.scale);
    this.field.redNum(this.ws2, 0,
        (3 * this.field.scale + this.field.scale) - this.field.scale * 51 / 16);
    this.field.state = 2;
    this.field.smiley(this.ws2, 3);
    t.checkExpect(this.field.makeScene(), this.ws2);

    this.InitData();
    this.field.array = this.testList;
    this.field.numMines = 5;
    this.field.border(this.ws2);
    // change numMines and the flags youre allotted should also increase
    this.field.redNum(this.ws2, 5, this.field.scale);
    this.field.redNum(this.ws2, 0,
        (3 * this.field.scale + this.field.scale) - this.field.scale * 51 / 16);
    this.field.state = 3;
    this.field.smiley(this.ws2, 4);
    t.checkExpect(this.field.makeScene(), this.ws2);
  }

  void testChangeDif(Tester t) {
    this.InitData();
    int length = (this.field.array.get(0).size() * 32 + (32 * 10) / 8);
    int scale = 32;
    t.checkExpect(this.ws, new WorldScene(1, 1));
    if (this.field.array.get(0).size() <= 12) {
      scale = 32 / 2;
    }
    int px = scale / 8;
    int height = 32 * 13 / 8;
    // Left
    this.ws2.placeImageXY(new RectangleImage(13 * px, 13 * px, "solid", Color.lightGray.darker()),
        length / 2 - 2 * scale, height);
    this.ws2.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(0, 12 * px),
        new Posn(12 * px, 0), "solid", Color.white), length / 2 - 2 * scale, height);
    this.ws2.placeImageXY(new RectangleImage(10 * px, 10 * px, "solid", Color.lightGray),
        length / 2 - 2 * scale, height);
    this.ws2.placeImageXY(new TextImage("B", scale, FontStyle.BOLD, Color.green),
        length / 2 - 2 * scale, height);

    // Middle
    this.ws2.placeImageXY(new RectangleImage(13 * px, 13 * px, "solid", Color.lightGray.darker()),
        length / 2, height);
    this.ws2.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(0, 12 * px),
        new Posn(12 * px, 0), "solid", Color.white), length / 2, height);
    this.ws2.placeImageXY(new RectangleImage(10 * px, 10 * px, "solid", Color.lightGray),
        length / 2, height);
    this.ws2.placeImageXY(new TextImage("I", scale, FontStyle.BOLD, Color.yellow), length / 2,
        height);

    // Right
    this.ws2.placeImageXY(new RectangleImage(13 * px, 13 * px, "solid", Color.lightGray.darker()),
        length / 2 + 2 * scale, height);
    this.ws2.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(0, 12 * px),
        new Posn(12 * px, 0), "solid", Color.white), length / 2 + 2 * scale, height);
    this.ws2.placeImageXY(new RectangleImage(10 * px, 10 * px, "solid", Color.lightGray),
        length / 2 + 2 * scale, height);
    this.ws2.placeImageXY(new TextImage("E", scale, FontStyle.BOLD, Color.red),
        length / 2 + 2 * scale, height);
    t.checkExpect(this.ws, this.ws2);
  }

  void testSmiley(Tester t) {
    this.InitData();
    int length = (this.field.array.get(0).size() * 32 + (32 * 10) / 8);
    int px = 32 / 8;
    this.ws2.placeImageXY(new RectangleImage(13 * px, 13 * px, "solid", Color.lightGray.darker()),
        length / 2, 13 * px);
    this.ws2.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(0, 12 * px),
        new Posn(12 * px, 0), "solid", Color.white), length / 2, 13 * px);
    this.ws2.placeImageXY(new RectangleImage(10 * px, 10 * px, "solid", Color.lightGray),
        length / 2, 13 * px);
    this.ws2.placeImageXY(new CircleImage(4 * px + px / 2, "solid", Color.black), length / 2,
        13 * px);
    this.ws2.placeImageXY(new CircleImage(4 * px, "solid", Color.yellow), length / 2, 13 * px);
    this.ws2.placeImageXY(new EllipseImage(4 * px + px / 2, px * 3, "solid", Color.black),
        length / 2, 14 * px);
    this.ws2.placeImageXY(new EllipseImage(4 * px, px * 3, "solid", Color.yellow), length / 2,
        13 * px + px / 2);
    this.ws2.placeImageXY(new CircleImage(px * 3 / 4, "solid", Color.black),
        length / 2 - px - px / 2, 12 * px);
    this.ws2.placeImageXY(new CircleImage(px * 3 / 4, "solid", Color.black),
        length / 2 + px + px / 2, 12 * px);
    this.field.smiley(this.ws, 1);
    t.checkExpect(this.ws, this.ws2);

    this.InitData();
    this.ws2.placeImageXY(new RectangleImage(13 * px, 13 * px, "solid", Color.lightGray.darker()),
        length / 2, 13 * px);
    this.ws2.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(0, 12 * px),
        new Posn(12 * px, 0), "solid", Color.white), length / 2, 13 * px);
    this.ws2.placeImageXY(new RectangleImage(10 * px, 10 * px, "solid", Color.lightGray),
        length / 2, 13 * px);
    this.ws2.placeImageXY(new CircleImage(4 * px + px / 2, "solid", Color.black), length / 2,
        13 * px);
    this.ws2.placeImageXY(new CircleImage(4 * px, "solid", Color.yellow), length / 2, 13 * px);
    this.ws2.placeImageXY(new CircleImage(px + px / 4, "solid", Color.black), length / 2, 15 * px);
    this.ws2.placeImageXY(new CircleImage(px * 3 / 4, "solid", Color.yellow), length / 2, 15 * px);
    this.ws2.placeImageXY(new CircleImage(px, "solid", Color.black), length / 2 - px - px / 2,
        12 * px);
    this.ws2.placeImageXY(new CircleImage(px, "solid", Color.black), length / 2 + px + px / 2,
        12 * px);
    this.field.smiley(this.ws, 2);
    t.checkExpect(this.ws, this.ws2);

    this.InitData();
    this.ws2.placeImageXY(new RectangleImage(13 * px, 13 * px, "solid", Color.lightGray.darker()),
        length / 2, 13 * px);
    this.ws2.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(0, 12 * px),
        new Posn(12 * px, 0), "solid", Color.white), length / 2, 13 * px);
    this.ws2.placeImageXY(new RectangleImage(10 * px, 10 * px, "solid", Color.lightGray),
        length / 2, 13 * px);
    this.ws2.placeImageXY(new CircleImage(4 * px + px / 2, "solid", Color.black), length / 2,
        13 * px);
    this.ws2.placeImageXY(new CircleImage(4 * px, "solid", Color.yellow), length / 2, 13 * px);
    this.ws2.placeImageXY(new EllipseImage(4 * px + px / 2, px * 3, "solid", Color.black),
        length / 2, 13 * px + px);
    this.ws2.placeImageXY(new EllipseImage(4 * px, px * 3, "solid", Color.yellow), length / 2,
        13 * px + px / 2);
    this.ws2.placeImageXY(new RectangleImage(5 * px, px, "solid", Color.yellow), length / 2,
        14 * px);
    this.ws2.placeImageXY(
        new RotateImage(new EllipseImage(2 * px, px + px / 2, "solid", Color.black), 315),
        length / 2 - px, 12 * px);
    this.ws2.placeImageXY(
        new RotateImage(new EllipseImage(2 * px, px + px / 2, "solid", Color.black), 45),
        length / 2 + px, 12 * px);
    this.ws2.placeImageXY(new RectangleImage(4 * px + px, px / 2, "solid", Color.black), length / 2,
        11 * px + px / 2);
    this.ws2.placeImageXY(new CircleImage(px, "solid", Color.black), length / 2 - px - px / 2,
        12 * px);
    this.ws2.placeImageXY(new CircleImage(px, "solid", Color.black), length / 2 + px + px / 2,
        12 * px);
    this.ws2.placeImageXY(
        new RotateImage(new RectangleImage(2 * px, px / 2, "solid", Color.black), 315),
        length / 2 - 3 * px, 12 * px + px / 4);
    this.ws2.placeImageXY(
        new RotateImage(new RectangleImage(2 * px + px / 2, px / 2, "solid", Color.black), 45),
        length / 2 + 3 * px + px / 2, 12 * px + px / 4);
    this.field.smiley(this.ws, 3);
    t.checkExpect(this.ws, this.ws2);

    this.InitData();
    this.ws2.placeImageXY(new RectangleImage(13 * px, 13 * px, "solid", Color.lightGray.darker()),
        length / 2, 13 * px);
    this.ws2.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(0, 12 * px),
        new Posn(12 * px, 0), "solid", Color.white), length / 2, 13 * px);
    this.ws2.placeImageXY(new RectangleImage(10 * px, 10 * px, "solid", Color.lightGray),
        length / 2, 13 * px);
    this.ws2.placeImageXY(new CircleImage(4 * px + px / 2, "solid", Color.black), length / 2,
        13 * px);
    this.ws2.placeImageXY(new CircleImage(4 * px, "solid", Color.yellow), length / 2, 13 * px);
    this.ws2.placeImageXY(
        new CropImage(0, 0, 4 * px + px / 2, px * 5 / 2,
            new EllipseImage(4 * px + px / 2, px * 3, "solid", Color.black)),
        length / 2, 15 * px - px / 4);
    this.ws2.placeImageXY(new CropImage(0, 0, 4 * px, px * 2,
        new EllipseImage(4 * px, px * 3, "solid", Color.yellow)), length / 2, 15 * px);
    this.ws2.placeImageXY(new TextImage("x", 32 / 2, FontStyle.BOLD, Color.black),
        length / 2 - px - px / 2, 12 * px - px / 2);
    this.ws2.placeImageXY(new TextImage("x", 32 / 2, FontStyle.BOLD, Color.black),
        length / 2 + px + px / 2, 12 * px - px / 2);
    this.field.smiley(this.ws, 4);
    t.checkExpect(this.ws, this.ws2);

    this.InitData();
    this.ws2.placeImageXY(new RectangleImage(13 * px, 13 * px, "solid", Color.lightGray.darker()),
        length / 2, 13 * px);
    this.ws2.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(0, 12 * px),
        new Posn(12 * px, 0), "solid", Color.white), length / 2, 13 * px);
    this.ws2.placeImageXY(new RectangleImage(10 * px, 10 * px, "solid", Color.lightGray),
        length / 2, 13 * px);
    this.ws2.placeImageXY(new CircleImage(4 * px + px / 2, "solid", Color.black), length / 2,
        13 * px);
    this.ws2.placeImageXY(new CircleImage(4 * px, "solid", Color.yellow), length / 2, 13 * px);
    this.ws2
        .placeImageXY(new FrameImage(new RectangleImage(12 * px, 12 * px, "solid", Color.lightGray),
            Color.lightGray.darker()), length / 2, 13 * px);
    this.ws2.placeImageXY(new CircleImage(4 * px + px / 2, "solid", Color.black),
        length / 2 + px / 4, 13 * px + px / 4);
    this.ws2.placeImageXY(new CircleImage(4 * px, "solid", Color.yellow), length / 2 + px / 4,
        13 * px + px / 4);
    this.ws2.placeImageXY(new EllipseImage(4 * px + px / 2, px * 3, "solid", Color.black),
        length / 2 + px / 4, 14 * px + px / 4);
    this.ws2.placeImageXY(new EllipseImage(4 * px, px * 3, "solid", Color.yellow),
        length / 2 + px / 4, 13 * px + px / 2 + px / 4);
    this.ws2.placeImageXY(new CircleImage(px * 3 / 4, "solid", Color.black),
        length / 2 - px - px / 4, 12 * px + px / 4);
    this.ws2.placeImageXY(new CircleImage(px * 3 / 4, "solid", Color.black),
        length / 2 + px + px / 2 + px / 4, 12 * px + px / 4);
    this.field.smiley(this.ws, 2);
    t.checkExpect(this.ws, this.ws2);
  }

  void testOnTick(Tester t) {
    this.InitData();
    this.field.onTick();
    t.checkExpect(this.field.timer, 1);
    this.field.onTick();
    t.checkExpect(this.field.timer, 2);
  }

  void testOnMousePressed(Tester t) {
    this.InitData();
    this.field.array = this.testList;
    this.field.numMines = 1;
    // Tests Hover
    t.checkExpect(this.node1.hover, false);
    this.field.onMousePressed(new Posn(30, 120), "LeftButton");
    t.checkExpect(this.node1.hover, true);

    // Tests the face
    this.InitData();
    this.field.array = this.testList;
    this.field.numMines = 1;
    t.checkExpect(this.field.isFacePressed, false);
    this.field.onMouseClicked(new Posn(30, 150), "LeftButton");
    this.field.onMousePressed(new Posn(60, 60), "LeftButton");
    t.checkExpect(this.field.isFacePressed, true);

  }

  void testOnMouseReleased(Tester t) {
    this.InitData();
    this.InitData();
    this.field.array = this.testList;
    this.field.numMines = 1;
    // Tests Hover
    t.checkExpect(this.node1.hover, false);
    this.field.onMousePressed(new Posn(30, 120), "LeftButton");
    t.checkExpect(this.node1.hover, true);
    // Give it a random place to show it releases node1
    this.field.onMouseReleased(new Posn(70, 120), "LeftButton");
    t.checkExpect(this.node1.hover, false);

    // Tests the face
    this.InitData();
    this.field.array = this.testList;
    this.field.numMines = 1;
    t.checkExpect(this.field.isFacePressed, false);
    this.field.onMouseClicked(new Posn(30, 150), "LeftButton");
    this.field.onMousePressed(new Posn(60, 60), "LeftButton");
    t.checkExpect(this.field.isFacePressed, true);
    this.field.onMouseReleased(new Posn(30, 120), "LeftButton");
    t.checkExpect(this.field.isFacePressed, false);
  }

  void testOnMouseClicked(Tester t) {
    this.InitData();
    this.field.array = this.testList;
    this.field.numMines = 1;
    // Tests general stuff and flags
    t.checkExpect(this.node1.flagged, false);
    this.field.onMouseClicked(new Posn(30, 120), "RightButton");
    t.checkExpect(this.node1.flagged, true);
    this.field.onMouseClicked(new Posn(30, 120), "LeftButton");
    t.checkExpect(this.node1.revealed, false);
    t.checkExpect(this.node2.revealed, false);
    t.checkExpect(this.node3.revealed, false);
    t.checkExpect(this.node4.revealed, false);
    t.checkExpect(this.node5.revealed, false);
    t.checkExpect(this.node6.revealed, false);
    t.checkExpect(this.node7.revealed, false);
    t.checkExpect(this.node8.revealed, false);
    t.checkExpect(this.node9.revealed, false);
    this.field.onMouseClicked(new Posn(62, 120), "LeftButton");
    t.checkExpect(this.node1.revealed, false);
    t.checkExpect(this.node2.revealed, true);
    t.checkExpect(this.node3.revealed, true);
    t.checkExpect(this.node4.revealed, true);
    t.checkExpect(this.node5.revealed, true);
    t.checkExpect(this.node6.revealed, true);
    t.checkExpect(this.node8.revealed, true);
    t.checkExpect(this.node9.revealed, true);

    this.InitData();
    this.field.array = this.testList;
    this.node7.isMine = true;
    this.field.numMines = 1;
    // tests mine relocating on first click
    t.checkExpect(this.node7.revealed, false);
    t.checkExpect(this.node7.isMine, true);
    this.field.onMouseClicked(new Posn(32, 195), "LeftButton");
    t.checkExpect(this.node7.revealed, true);
    t.checkExpect(this.node7.isMine, false);
    this.field.onMouseClicked(new Posn(32, 120), "LeftButton");
    t.checkExpect(this.node1.revealed, true);
    t.checkExpect(this.node1.isMine, true);
    t.checkExpect(this.field.state, 3);

    // testing Beginner button
    this.InitData();
    this.field.array = this.testList;
    this.node7.isMine = true;
    this.field.numMines = 1;
    t.checkExpect(this.field.array.size(), 3);
    t.checkExpect(this.field.array.get(0).size(), 3);
    t.checkExpect(this.field.numMines, 1);
    this.field.onMouseClicked(new Posn(45, 60), "LeftButton");
    t.checkExpect(this.field.array.size(), 9);
    t.checkExpect(this.field.array.get(0).size(), 9);
    t.checkExpect(this.field.numMines, 10);
    // testing Intermediete button
    this.InitData();
    this.field.array = this.testList;
    this.node7.isMine = true;
    this.field.numMines = 1;
    t.checkExpect(this.field.array.size(), 3);
    t.checkExpect(this.field.array.get(0).size(), 3);
    t.checkExpect(this.field.numMines, 1);
    this.field.onMouseClicked(new Posn(60, 60), "LeftButton");
    t.checkExpect(this.field.array.size(), 16);
    t.checkExpect(this.field.array.get(0).size(), 16);
    t.checkExpect(this.field.numMines, 40);
    // testing Expert button
    this.InitData();
    this.field.array = this.testList;
    this.node7.isMine = true;
    this.field.numMines = 1;
    t.checkExpect(this.field.array.size(), 3);
    t.checkExpect(this.field.array.get(0).size(), 3);
    t.checkExpect(this.field.numMines, 1);
    this.field.onMouseClicked(new Posn(95, 60), "LeftButton");
    t.checkExpect(this.field.array.size(), 16);
    t.checkExpect(this.field.array.get(0).size(), 30);
    t.checkExpect(this.field.numMines, 99);
    // testing reset button
    this.InitData();
    this.field.array = this.testList;
    this.node7.isMine = true;
    this.field.numMines = 1;
    t.checkExpect(this.field.array.size(), 3);
    t.checkExpect(this.field.array.get(0).size(), 3);
    t.checkExpect(this.field.numMines, 1);
    t.checkExpect(this.field.firstClick, true);
    this.field.onMouseClicked(new Posn(60, 120), "LeftButton");
    t.checkExpect(this.field.firstClick, false);
    this.field.onMouseClicked(new Posn(60, 60), "LeftButton");
    t.checkExpect(this.field.array.size(), 3);
    t.checkExpect(this.field.array.get(0).size(), 3);
    t.checkExpect(this.field.firstClick, true);
  }

  void testRedNum(Tester t) {
    this.InitData();
    this.field.redNum(this.ws, 10, 0);
    int px = (32 / 8);
    this.ws2.placeImageXY(new RectangleImage(px * 39 / 2, px * 23 / 2, "solid", Color.black),
        (px * 39 / 4) + 0, (px * 23 / 4) + px * 7);
    this.field.digit(this.ws2, 10 / 100, 0);
    this.field.digit(this.ws2, (10 % 100) / 10, 0 + 32 * 27 / 32);
    this.field.digit(this.ws2, (10 % 100) % 10, 0 + 32 * 27 / 16);
    t.checkExpect(this.ws, this.ws2);

    this.InitData();
    this.field.redNum(ws, -10, 0);
    this.ws2.placeImageXY(new RectangleImage(px * 39 / 2, px * 23 / 2, "solid", Color.black),
        (px * 39 / 4) + 0, (px * 23 / 4) + px * 7);
    this.field.digit(this.ws2, -1, 0);
    this.field.digit(this.ws2, (-10 % 100) / -10, 0 + 32 * 27 / 32);
    this.field.digit(this.ws2, (-10 % 100) % 10 * -1, 0 + 32 * 27 / 16);
    t.checkExpect(this.ws, this.ws2);

    this.InitData();
    this.field.redNum(ws, -100, 0);
    this.ws2.placeImageXY(new RectangleImage(px * 39 / 2, px * 23 / 2, "solid", Color.black),
        (px * 39 / 4) + 0, (px * 23 / 4) + px * 7);
    this.field.digit(this.ws2, -1, 0);
    this.field.digit(this.ws2, 9, 0 + 32 * 27 / 32);
    this.field.digit(this.ws2, 9, 0 + 32 * 27 / 16);
    t.checkExpect(this.ws, this.ws2);

    this.InitData();
    this.field.redNum(ws, 1000, 0);
    this.ws2.placeImageXY(new RectangleImage(px * 39 / 2, px * 23 / 2, "solid", Color.black),
        (px * 39 / 4) + 0, (px * 23 / 4) + px * 7);
    this.field.digit(this.ws2, 9, 0);
    this.field.digit(this.ws2, 9, 0 + 32 * 27 / 32);
    this.field.digit(this.ws2, 9, 0 + 32 * 27 / 16);
    t.checkExpect(this.ws, this.ws2);
  }

  // Will never have a number outside of the range of [-1, 9]
  void testDigit(Tester t) {
    Color on = Color.red;
    Color off = Color.red.darker().darker();
    int miniPx = 32 / 16;
    WorldImage trapezoidOn = new BesideImage(
        new TriangleImage(new Posn(0, 0), new Posn(miniPx * 3, 0), new Posn(miniPx * 3, miniPx * 3),
            "solid", on),
        new RectangleImage(miniPx * 3, miniPx * 3, "solid", on), new TriangleImage(new Posn(0, 0),
            new Posn(miniPx * 3, 0), new Posn(0, miniPx * 3), "solid", on));
    WorldImage trapezoidOff = new BesideImage(
        new TriangleImage(new Posn(0, 0), new Posn(miniPx * 3, 0), new Posn(miniPx * 3, miniPx * 3),
            "solid", off),
        new RectangleImage(miniPx * 3, miniPx * 3, "solid", off), new TriangleImage(new Posn(0, 0),
            new Posn(miniPx * 3, 0), new Posn(0, miniPx * 3), "solid", off));
    WorldImage hexagonOn = new BesideImage(
        new TriangleImage(new Posn(0, 0), new Posn(miniPx, miniPx), new Posn(miniPx, miniPx * -1),
            "solid", on),
        new RectangleImage(miniPx * 3, miniPx * 3, "solid", on), new TriangleImage(
            new Posn(miniPx, 0), new Posn(0, miniPx), new Posn(0, miniPx * -1), "solid", on));
    WorldImage hexagonOff = new BesideImage(
        new TriangleImage(new Posn(0, 0), new Posn(miniPx, miniPx), new Posn(miniPx, miniPx * -1),
            "solid", off),
        new RectangleImage(miniPx * 3, miniPx * 3, "solid", off), new TriangleImage(
            new Posn(miniPx, 0), new Posn(0, miniPx), new Posn(0, miniPx * -1), "solid", off));
    this.InitData();
    this.ws2.placeImageXY(trapezoidOn, miniPx * 6, 32 + miniPx);
    this.ws2.placeImageXY(new RotateImage(trapezoidOn, 90), miniPx * 9, 32 + miniPx * 5);
    this.ws2.placeImageXY(new RotateImage(trapezoidOn, 270), miniPx * 3, 32 + miniPx * 5);
    this.ws2.placeImageXY(new RotateImage(trapezoidOn, 90), miniPx * 9, 32 + miniPx * 14);
    this.ws2.placeImageXY(new RotateImage(trapezoidOn, 270), miniPx * 3, 32 + miniPx * 14);
    this.ws2.placeImageXY(new RotateImage(trapezoidOn, 180), miniPx * 6, 32 + miniPx * 18);
    this.ws2.placeImageXY(hexagonOff, miniPx * 6, 32 + miniPx * 19 / 2);

    this.field.digit(this.ws, 0, 0);
    t.checkExpect(this.ws, this.ws2);

    this.InitData();
    this.ws2.placeImageXY(trapezoidOff, miniPx * 6, 32 + miniPx);
    this.ws2.placeImageXY(new RotateImage(trapezoidOff, 90), miniPx * 9, 32 + miniPx * 5);
    this.ws2.placeImageXY(new RotateImage(trapezoidOff, 270), miniPx * 3, 32 + miniPx * 5);
    this.ws2.placeImageXY(new RotateImage(trapezoidOff, 90), miniPx * 9, 32 + miniPx * 14);
    this.ws2.placeImageXY(new RotateImage(trapezoidOff, 270), miniPx * 3, 32 + miniPx * 14);
    this.ws2.placeImageXY(new RotateImage(trapezoidOff, 180), miniPx * 6, 32 + miniPx * 18);
    this.ws2.placeImageXY(hexagonOn, miniPx * 6, 32 + miniPx * 19 / 2);

    this.field.digit(this.ws, -1, 0);
    t.checkExpect(this.ws, this.ws2);

    this.InitData();
    this.ws2.placeImageXY(trapezoidOn, miniPx * 6, 32 + miniPx);
    this.ws2.placeImageXY(new RotateImage(trapezoidOn, 90), miniPx * 9, 32 + miniPx * 5);
    this.ws2.placeImageXY(new RotateImage(trapezoidOn, 270), miniPx * 3, 32 + miniPx * 5);
    this.ws2.placeImageXY(new RotateImage(trapezoidOn, 90), miniPx * 9, 32 + miniPx * 14);
    this.ws2.placeImageXY(new RotateImage(trapezoidOff, 270), miniPx * 3, 32 + miniPx * 14);
    this.ws2.placeImageXY(new RotateImage(trapezoidOn, 180), miniPx * 6, 32 + miniPx * 18);
    this.ws2.placeImageXY(hexagonOff, miniPx * 6, 32 + miniPx * 19 / 2);

    this.field.digit(this.ws, 9, 0);
    t.checkExpect(this.ws, this.ws2);
  }

  // Graphical
  void testBorder(Tester t) {
    this.InitData();
    t.checkExpect(this.ws, new WorldScene(1, 1));
    this.field.border(this.ws);
    int length = (30 * 32 + (32 * 10) / 8);
    int height = (30 + 2) * 32 + (32 * 15) / 8;
    // left/upper rim
    this.ws2.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(length, 0),
        new Posn(0, height), "solid", Color.white), length / 2, height / 2);
    // right/lower rim
    this.ws2.placeImageXY(new TriangleImage(new Posn(length, height), new Posn(length, 0),
        new Posn(0, height), "solid", Color.lightGray.darker()), length / 2, height / 2);
    // border fix
    this.ws2.placeImageXY(new TriangleImage(new Posn(32, 32), new Posn(32, 0), new Posn(0, 32),
        "solid", Color.lightGray.darker()), 32 / 2, height - 32 / 2);
    // border
    this.ws2.placeImageXY(
        new RectangleImage(length - (32 / 4), height - 32 / 4, "solid", Color.lightGray),
        length / 2, height / 2);
    // top panel left/upper rim
    this.ws2.placeImageXY(
        new TriangleImage(new Posn(0, 0), new Posn(length - (32 * 10 / 8) + 32 / 4, 0),
            new Posn(0, 2 * 32 + 32 / 4), "solid", Color.lightGray.darker()),
        length / 2, (2 * 32 + (32 * 10) / 8) / 2);
    // top panel right/lower rim
    this.ws2
        .placeImageXY(new TriangleImage(new Posn(length - (32 * 10 / 8) + 32 / 4, 2 * 32 + 32 / 4),
            new Posn(length - (32 * 10 / 8) + 32 / 4, 0), new Posn(0, 2 * 32 + 32 / 4), "solid",
            Color.white), length / 2, (2 * 32 + (32 * 10) / 8) / 2);
    // top panel fixes
    this.ws2.placeImageXY(new TriangleImage(new Posn(0, 0), new Posn(32 * 2, 0),
        new Posn(0, 32 * 2), "solid", Color.lightGray.darker()), length - (32 * 3 / 2),
        (32 * 3 / 2));
    this.ws2.placeImageXY(new TriangleImage(new Posn(32 * 2, 32 * 2), new Posn(32 * 2, 0),
        new Posn(0, 32 * 2), "solid", Color.white), 32 * 3 / 2, 32 * 14 / 8);
    // top panel
    this.ws2.placeImageXY(
        new RectangleImage(length - (32 * 10 / 8), 2 * 32, "solid", Color.lightGray), length / 2,
        (2 * 32 + (32 * 10) / 8) / 2);
    // bottom panel left/upper rim
    this.ws2.placeImageXY(
        new TriangleImage(new Posn(0, 0), new Posn(length - (32 * 10 / 8) + 32 / 4, 0),
            new Posn(0, 30 * 32 + 32 / 4), "solid", Color.lightGray.darker()),
        length / 2, (height + (2 * 32 + 32 * 5 / 8)) / 2);
    // bottom panel right/lower rim
    this.ws2
        .placeImageXY(new TriangleImage(new Posn(length - (32 * 10 / 8) + 32 / 4, 30 * 32 + 32 / 4),
            new Posn(length - (32 * 10 / 8) + 32 / 4, 0), new Posn(0, 30 * 32 + 32 / 4), "solid",
            Color.white), length / 2, (height + (2 * 32 + 32 * 5 / 8)) / 2);
    // bottom panel fixes
    this.ws2.placeImageXY(
        new TriangleImage(new Posn(0, 0), new Posn(32 * 2, 0), new Posn(0, 32 * 2), "solid",
            Color.lightGray.darker()),
        length - (32 * 3 / 2), (2 * 32 + (32 * 10) / 8) + 32 * 7 / 8);
    this.ws2.placeImageXY(
        new TriangleImage(new Posn(32, 32), new Posn(32, 0), new Posn(0, 32), "solid", Color.white),
        32, height - 32);
    // mines background
    this.ws2.placeImageXY(new RectangleImage(length - (32 * 10 / 8), 30 * 32, "solid", Color.white),
        length / 2, (height + (2 * 32 + 32 * 5 / 8)) / 2);
    t.checkExpect(this.ws, this.ws2);
  }

  void testMakeField(Tester t) {
    this.InitData();
    this.field.array.clear();
    this.field.makeField(16, 16);
    t.checkExpect(this.field.array.size(), 16);
    t.checkExpect(this.field.array.get(0).size(), 16);
    this.InitData();
    this.field.array.clear();
    this.field.makeField(30, 16);
    t.checkExpect(this.field.array.size(), 16);
    t.checkExpect(this.field.array.get(0).size(), 30);
    this.InitData();
    this.field.array.clear();
    this.field.makeField(0, 0);
    t.checkExpect(this.field.array.size(), 0);
    // You can't even try to get this one's 2D size
    t.checkException(new IndexOutOfBoundsException("Index 0 out of bounds for length 0"),
        this.field.array, "get", 0);
  }

  void testmakeMines(Tester t) {
    this.InitData();
    this.field.array.clear();
    this.field.makeField(0, 0);
    this.field.makeMines(0, this.rand);
    int count = 0;
    for (ArrayList<Node> i : this.field.array) {
      for (Node j : i) {
        if (j.isMine) {
          count = count + 1;
        }
      }
    }
    t.checkExpect(count, 0);
  }

  void testDrawNodes(Tester t) {

    this.InitData();
    t.checkExpect(this.ws, ws2);
    this.node1.isMine = true;
    this.node5.revealed = true;
    int posX = (10 * 32) + (32 * 1 / 2) + (32 * 5) / 8;
    int posY = ((10 + 3) * 32) + ((32 * 3) / 4);
    this.ws2
        .placeImageXY(new FrameImage(new RectangleImage(32 - 1, 32 - 1, "solid", Color.lightGray),
            Color.lightGray.darker()), posX, posY);
    this.ws2.placeImageXY(new TextImage(Integer.toString(1), 32, Color.blue), posX, posY);
    t.checkExpect(this.node5.drawNodes(this.ws, 10, 10, 32), this.ws2);

    this.InitData();
    this.ws2 = new WorldScene(1, 1);
    t.checkExpect(this.ws, this.ws2);
    this.ws2
        .placeImageXY(new FrameImage(new RectangleImage(32 - 1, 32 - 1, "solid", Color.lightGray),
            Color.lightGray.darker()), posX, posY);
    t.checkExpect(this.node5.drawNodes(this.ws, 10, 10, 32), this.ws2);

    this.InitData();
    this.ws2 = new WorldScene(1, 1);
    t.checkExpect(this.ws, this.ws2);
    this.node1.isMine = true;
    this.node2.isMine = true;
    this.node3.isMine = true;
    this.node5.revealed = true;
    this.node5.drawNodes(this.ws, 10, 10, 32);
    this.ws2
        .placeImageXY(new FrameImage(new RectangleImage(32 - 1, 32 - 1, "solid", Color.lightGray),
            Color.lightGray.darker()), posX, posY);
    this.ws2.placeImageXY(new TextImage(Integer.toString(3), 32, Color.red), posX, posY);
    t.checkExpect(this.node5.drawNodes(this.ws, 10, 10, 32), this.ws2);

    this.InitData();
    this.ws2 = new WorldScene(1, 1);
    t.checkExpect(this.ws, ws2);
    this.node1.isMine = true;
    this.node2.isMine = true;
    this.node3.isMine = true;
    this.node4.isMine = true;
    this.node6.isMine = true;
    this.node7.isMine = true;
    this.node8.isMine = true;
    this.node9.isMine = true;
    this.node5.revealed = true;
    this.node5.drawNodes(this.ws, 10, 10, 32);
    this.ws2
        .placeImageXY(new FrameImage(new RectangleImage(32 - 1, 32 - 1, "solid", Color.lightGray),
            Color.lightGray.darker()), posX, posY);
    this.ws2.placeImageXY(new TextImage(Integer.toString(8), 32, Color.darkGray), posX, posY);
    t.checkExpect(this.node5.drawNodes(this.ws, 10, 10, 32), this.ws2);

    this.InitData();
    int scale = 32;
    t.checkExpect(this.ws, this.ws2);
    this.node5.isMine = true;
    this.node5.revealed = true;
    this.ws2.placeImageXY(
        new FrameImage(new RectangleImage((10 - 1), (10 - 1), "solid", Color.lightGray),
            Color.lightGray.darker()),
        posX, posY);
    // Mine
    this.ws2.placeImageXY(new CircleImage(scale * 9 / 32, "solid", Color.black), posX, posY);
    this.ws2.placeImageXY(new LineImage(new Posn(0, scale * 3 / 4), Color.black), posX, posY);
    this.ws2.placeImageXY(new LineImage(new Posn(scale * 3 / 4, 0), Color.black), posX, posY);
    this.ws2.placeImageXY(
        new RotateImage(new LineImage(new Posn(scale * 3 / 4, 0), Color.black), 45), posX, posY);
    this.ws2.placeImageXY(
        new RotateImage(new LineImage(new Posn(scale * 3 / 4, 0), Color.black), 315), posX, posY);
    this.ws2.placeImageXY(new CircleImage(scale / 8, "solid", Color.white), posX - (scale / 16),
        posY - (scale / 16));
    t.checkExpect(this.node5.drawNodes(this.ws, 10, 10, 32), this.ws2);

    this.InitData();
    t.checkExpect(this.ws, this.ws2);
    this.node5.isMine = true;
    this.node5.loseMine = true;
    this.node5.revealed = true;
    this.ws2.placeImageXY(new FrameImage(new RectangleImage((10 - 1), (10 - 1), "solid", Color.red),
        Color.lightGray.darker()), posX, posY);
    // Mine
    this.ws2.placeImageXY(new CircleImage(scale * 9 / 32, "solid", Color.black), posX, posY);
    this.ws2.placeImageXY(new LineImage(new Posn(0, scale * 3 / 4), Color.black), posX, posY);
    this.ws2.placeImageXY(new LineImage(new Posn(scale * 3 / 4, 0), Color.black), posX, posY);
    this.ws2.placeImageXY(
        new RotateImage(new LineImage(new Posn(scale * 3 / 4, 0), Color.black), 45), posX, posY);
    this.ws2.placeImageXY(
        new RotateImage(new LineImage(new Posn(scale * 3 / 4, 0), Color.black), 315), posX, posY);
    this.ws2.placeImageXY(new CircleImage(scale / 8, "solid", Color.white), posX - (scale / 16),
        posY - (scale / 16));
    t.checkExpect(this.node5.drawNodes(this.ws, 10, 10, 32), this.ws2);

    this.InitData();
    t.checkExpect(this.ws, this.ws2);
    this.node5.flagged = true;
    this.node5.revealed = true;
    this.ws2.placeImageXY(new RectangleImage(scale / 16, scale / 2, "solid", Color.black),
        posX + scale / 32, posY);
    this.ws2.placeImageXY(
        new TriangleImage(new Posn(0, 0), new Posn(scale * 3 / 8, scale * 3 / 16),
            new Posn(scale * 3 / 8, scale * 3 / -16), "solid", Color.red),
        posX - scale / 8, posY - scale / 8);
    this.ws2.placeImageXY(new RectangleImage(scale / 4, scale / 16, "solid", Color.black), posX,
        posY + scale * 3 / 16);
    this.ws2.placeImageXY(new RectangleImage(scale / 2, scale / 8, "solid", Color.black), posX,
        posY + scale * 4 / 16);
    t.checkExpect(this.node5.drawNodes(this.ws, 10, 10, 32), this.ws2);

    this.InitData();
    t.checkExpect(this.ws, this.ws2);
    this.node5.flagged = true;
    this.node5.revealed = true;
    ws.placeImageXY(
        new FrameImage(new RectangleImage((scale - 1), (scale - 1), "solid", Color.lightGray),
            Color.lightGray.darker()),
        posX, posY);
    // Mine
    this.ws2.placeImageXY(new CircleImage(scale * 9 / 32, "solid", Color.black), posX, posY);
    this.ws2.placeImageXY(new LineImage(new Posn(0, scale * 3 / 4), Color.black), posX, posY);
    this.ws2.placeImageXY(new LineImage(new Posn(scale * 3 / 4, 0), Color.black), posX, posY);
    this.ws2.placeImageXY(new LineImage(new Posn(scale * 3 / 4, scale * 3 / 4), Color.black), posX,
        posY);
    this.ws2.placeImageXY(new LineImage(new Posn(-scale * 3 / 4, scale * 3 / 4), Color.black), posX,
        posY);
    this.ws2.placeImageXY(new CircleImage(scale / 8, "solid", Color.white), posX - (scale / 16),
        posY - (scale / 16));
    this.ws2
        .placeImageXY(new RotateImage(
            new ShearedImage(
                new RectangleImage(scale, scale * 3 / 32, OutlineMode.SOLID, Color.red), -1.5, 0),
            45), posX, posY);
    this.ws2
        .placeImageXY(new RotateImage(
            new ShearedImage(
                new RectangleImage(scale * 3 / 32, scale, OutlineMode.SOLID, Color.red), 0, -1.5),
            45), posX, posY);
    t.checkExpect(this.node5.drawNodes(this.ws, 10, 10, 32), this.ws2);

  }

  void testCountMines(Tester t) {
    this.InitData();
    this.node1.isMine = true;
    this.node3.isMine = true;
    t.checkExpect(this.node5.countMines(), 2);
    t.checkExpect(this.node4.countMines(), 1);
    t.checkExpect(this.node7.countMines(), 0);
  }

  void testMassReveal(Tester t) {
    // Not touching a mine
    this.InitData();
    this.node1.isMine = true;
    this.node3.isMine = true;
    this.node8.massReveal();
    t.checkExpect(this.node1.revealed, false);
    t.checkExpect(this.node2.revealed, false);
    t.checkExpect(this.node3.revealed, false);
    t.checkExpect(this.node4.revealed, true);
    t.checkExpect(this.node5.revealed, true);
    t.checkExpect(this.node6.revealed, true);
    t.checkExpect(this.node7.revealed, true);
    t.checkExpect(this.node8.revealed, true);
    t.checkExpect(this.node9.revealed, true);

    // touching a mine
    this.InitData();
    this.node1.isMine = true;
    this.node4.isMine = true;
    this.node8.massReveal();
    t.checkExpect(this.node1.revealed, false);
    t.checkExpect(this.node2.revealed, false);
    t.checkExpect(this.node3.revealed, false);
    t.checkExpect(this.node4.revealed, false);
    t.checkExpect(this.node5.revealed, false);
    t.checkExpect(this.node6.revealed, false);
    t.checkExpect(this.node7.revealed, false);
    t.checkExpect(this.node8.revealed, true);
    t.checkExpect(this.node9.revealed, false);

    // different code handles if you are on a mine;
  }

//  // calls big bang to launch the task manager
//  void testBigBang(Tester t) {
//    // each pixel is 4, each scale is 1 full 8px box
//    // Give or take 10 pixels on the border
//    // Inner box is 2 wide
//    // Outer border in 5 each
//    // timer is 11.5 x 19.5 pixels tall
//    // I did pixel measurements for these
//    int scale = 32; // must be a multiple of 8
//    int col = 30;
//    int row = 16;
//    int worldWidth = col * scale + ((10 * scale) / 8);
//    int worldHeight = (row + 2) * scale + ((15 * scale) / 8);
//    double tickRate = .1;
//    Field world = new Field(col, row, 99, scale);
//    world.bigBang(worldWidth, worldHeight, tickRate);
//  }
}

class hw9{
  public static void main(String[] args) {
    int scale = 32; // must be a multiple of 8
    int col = 30;
    int row = 16;
    int worldWidth = col * scale + ((10 * scale) / 8);
    int worldHeight = (row + 2) * scale + ((15 * scale) / 8);
    double tickRate = .1;
    Field world = new Field(col, row, 99, scale);
    world.bigBang(worldWidth, worldHeight, tickRate); 
  }
}