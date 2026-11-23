# Farming Game

## Team Information

### Team Members

- Kaho Yamamoto

- Shuting Wu

- Angela Peng

- Bella Patel


---

## Project Description

### Domain

entertainment

---

## User Stories

### 1. As a player, I want to create and log in to my account to save my progress on the game. 

### 2. As a player, I want to plant flowers in my flower plots so I can make progress in the game.

### 3. As a player, I want to grow my flower from the small stage to the big stage.

### 4. As a player, I want to harvest my fully grown flowers so that they are counted in my Gallery.

### 5. As a player, I want to see a leaderboard of harvest counts and choose 
### whether my own count is public or private,so that I can compare with others while 
### controlling what I share.

---

## Use Cases

### User story 1: Login
Use Case 1
- Name: Log in to game
- Primary actor: Player
- Precondition: Game is initially loaded
- Trigger: The player inputs username and password and clicks the "login" button
- Main success scenario:
  1. Player inputs username and password.
  2. Player clicks "login" button.
  3. System checks that an account with that information exists.
  4. System finds user information.
  5. System loads main page updated with user information.
- Extension:
  - Player inputs username and password and clicks login, but there is no user with that information so an error shows.
  - Player inputs username and password and clicks signup, but there is already a user with the inputted username, so the system shows error message.
  - Player inputs a unique username and password and clicks signup, so the system creates an account with that information and displays the login page again.
- Success outcome: The main page is displayed with user information.

### User story 2: Plant flower
Use Case 2
- Name: Plant a Flower
- Primary actor: Player
- Precondition: The player has at least one empty plot in the garden.
- Trigger: The player clicks an empty plot.
- Main success scenario:
    1. Player clicks an empty plot.
    2. System shows a menu for the plot with 'Plant' as the only available action.
    3. Player clicks the 'Plant' button
    4. The menu disappears and the system shows the plot occupied by a small flower.
- Extensions:
    - 2a. Player cancels, so the plot stays empty.
    - 2b. The selected plot is not empty, so the option for 'Plant' is not available (addressed in Use Case 3)
- Success outcome: The plot contains a baby flower.


### User Story 3: Grow flower
Use Case 3
- Name: Grow a Flower
- Primary actor: Player
- Precondition: The player is logged in and viewing their farm. The plot contains a flower in the small stage.
- Trigger: The player clicks the plot containing the small flower.
- Main success scenario:
  1. System opens a menu for the plot with 'Grow' as the only available action.
  2. Player selects 'Grow'.
  3. System changes the flower from the small stage to the big stage.
  4. System displays the big flower in the same plot.
- Extensions:
  - 2a. Player dismisses the menu. System closes the menu and returns to the farm view. The use case ends.
- Success outcome: The flower in the selected plot is in the big stage.

### User Story 4: Harvest flower
Use Case 4
- Name: Harvest a Flower
- Primary actor: Player
- Precondition: The player is logged in and the selected plot has a big flower.
- Trigger: The player clicks a plot with a big flower.
- Main success scenario:
  1. Player clicks a plot with a big flower.
  2. System shows the prompt "harvest flower at plot N".
  3. Player clicks the prompt to confirm.
  4. System sets the plot to empty and adds one to the player's harvest count.
  5. System shows the updated grid and the new harvest count.
- Extensions:
  - None.
- Success outcome: The plot is empty and the player's harvest count has increased by one.

### User Story 5: Leaderboard
Use Case 5
- Name: View Leaderboard
- Primary actor: Player
- Precondition: The player is logged in.
- Trigger: The player clicks "Leaderboard".
- Main success scenario:
  1. Player clicks "Leaderboard".
  2. System checks that the player has already chosen whether their harvest count is public or private.
  3. If the count is public, system uploads the player's username and current harvest count, replacing any earlier entry from this player.
  4. System gets all uploaded entries from the online leaderboard service.
  5. System sorts the entries by harvest count from highest to lowest.
  6. System shows the top three usernames and counts, the player's own harvest count, and whether it is currently Public or Private.
- Extensions:
  - 2a. The player has not chosen yet, so the system asks the player to choose Public or Private, saves the choice, and continues at step 3.
  - 3a. The upload fails (for example, no internet), so the system tells the player and continues at step 4.
  - 4a. The service cannot be reached, so the system tells the player and shows only the player's own count.
  - 6a. There are fewer than three entries, so the system shows all of them.
  - 6b. The player switches between Public and Private, so the system saves the new choice, uploads or removes the player's entry accordingly, and shows the updated leaderboard.
- Success outcome: The player sees the current top three. The player's choice is saved, and the online leaderboard matches it.
---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |

| Kaho | Use Case 1 |

| Bella | Use Case 2 |

| Angela | Use Case 3 |

| Tiffany | Use Case 4: Harvest a Flower, Use Case 5: View Leaderboard |

---

## Entities

### Plot
- Instance variables:
  - status: String ("empty", "small", or "big"), starts as "empty"
- Methods:
  - getStatus(): String
  - plant(): sets status from "empty" to "small"
  - grow(): sets status from "small" to "big"
  - harvest(): sets status from "big" to "empty"

### HarvestRecord
- Instance variables:
  - username: String
  - harvestCount: int, starts at 0
  - visibility: String ("unset", "public", or "private"), starts as "unset"
- Methods:
  - getUsername(): String
  - getHarvestCount(): int
  - incrementHarvestCount(): increases the count by one
  - getVisibility(): String
  - setVisibility(String): sets the visibility

### LeaderboardEntry
- Instance variables:
  - username: String
  - harvestCount: int
- Methods:
  - getUsername(): String
  - getHarvestCount(): int

### Entities

(Add additional entities as needed.)

---

## API Usage

**API Name:** Firebase Realtime Database REST API

**Documentation:** https://firebase.google.com/docs/reference/rest/database/

**How it is used in the program:** The leaderboard feature uses this API as an online scoreboard. 
  When a player chooses to participate, the program uploads the player's username and harvest count
  (a PUT request that replaces any earlier entry). When the player switches to view-only,
  the program removes the player's entry (a DELETE request). 
  To show the leaderboard, the program reads all entries (a GET request), 
  sorts them by harvest count, and displays the top three. 
  All requests are sent from the data access layer, 
  so the rest of the program does not depend on this service.

**Successfully called:** Yes. A test program wrote two entries, read them back, deleted one, and read again, 
                          and all requests returned status 200.

---

## Design Diagrams

### UML Class Diagram

### Sequence Diagrams

---

## How to Run

[Instructions for running the program.]

---

## Notes

### Visual Project Outline
![Project Outline Diagram](images/project_outline.png)

