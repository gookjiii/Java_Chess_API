# Java_Chess_API
Simple Java Chess API

## Table of Contents

- [Introduction](#introduction)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Introduction

Java Chessgame API endless board. Extra rule: MegaPawn

## Installation

**Clone the Repository:**

   ```bash
   git clone https://github.com/gookjiii/Java_Chess_API/
   ```
## Usage
**Create board**
  ```bash
  Board board = new Board(megaPawnList);
  ```
**Add figures**
  ```bash
  board.putPiece(new King(true), 1, 1, true);
  ```
**Add listeners**
  ```bash
    CheckListener checkListener = new CheckListenerImpl();
    CheckmateListener checkmateListener = new CheckmateListenerImpl();
    board.addCheckListener(checkListener);
    board.addCheckmateListener(checkmateListener);
  ```
**Move a figure**
  ```bash
  board.move(board.getSpot(x, y), board.getSpot(a, b));
  ```
**Get a figure from coordinate**
  ```bash
  board.getFigure(x, y);
  ```


