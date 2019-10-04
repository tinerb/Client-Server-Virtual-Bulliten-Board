package board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SBoard {

	public static int width;
	public static int height;
	public static String[] colours;
	public static Board board;

	// args starts reading from index 0 meaning the port number is the first
	// argument
	public static void main(String[] args) throws Exception {

		// assign port number
		int portNumber = Integer.parseInt(args[0]);
		// assign dimensions
		width = Integer.parseInt(args[1]);
		height = Integer.parseInt(args[2]);
		int colourLen = args.length - 3;
		colours = new String[colourLen];
		// create colour array
		for (int i = 0; i < colourLen; i++) {
			colours[i] = args[i + 3];
		}
		System.out.println("The board server is running.");
		ServerSocket listener = new ServerSocket(portNumber);

		board = new Board(width, height, colours);

		try {
			while (true) {
				new Boards(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	public static class Pin {
		public int x;
		public int y;

		public Pin(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public static class Note {
		public String content;
		public boolean isPinned;
		public String colour;
		public ArrayList<Pin> points = new ArrayList<Pin>();
		public Pin point;
		public int width;
		public int height;

		public Note(String content, String colour, int x, int y, int width, int height) {
			this.content = content;
			this.isPinned = false;
			this.colour = colour;
			this.point = new Pin(x, y);
			this.width = width;
			this.height = height;
		}

		public void setPinned(Note n) {
			n.isPinned = true;
		}
	}

	public static class Board {
		private int width;
		private int height;
		private String[] colours;
		public ArrayList<Pin> pins = new ArrayList<Pin>();
		public ArrayList<Note> notes = new ArrayList<Note>();

		public Board(int width, int height, String[] colours) {
			this.width = width;
			this.height = height;
			this.colours = colours.clone();
		}
	}

	private static class Boards extends Thread {
		private Socket socket;

		public Boards(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

				while (true) {
					// THE CODE BELOW CURRENTLY DOES NOT SEND BACK TO CLIENT
					String input = in.readLine();
					String[] parsed = input.split("@@");

					// I think this is all of the button functionality
					// double check if it is and delete this comment once you have
					// add stuff to the board by: board.[method]
					if (parsed[0].equals("pin")) {
						int x = Integer.parseInt(parsed[1]);
						int y = Integer.parseInt(parsed[2]);
						Pin p = new Pin(x, y);
						boolean on = isPinOnBoard(p);
						if (on) {
							board.pins.add(p);
							updateNotes(p, board.notes);
							out.println("Pin " + x + " " + y + " successful.");
						} else {
							// send client error message that requested pin is off the board bounds
							out.println("Pin out of board boundaries.");
						}
					} else if (parsed[0].equals("unpin")) {
						int x = Integer.parseInt(parsed[1]);
						int y = Integer.parseInt(parsed[2]);
						Pin p = new Pin(x, y);
						int result = removePin(p, board.pins);
						if (result == 1) {
							updateUnpin(p, board.notes);
							out.println("Removal of pin " + x + " " + y + " is successful.");
						} else {
							out.println(
									"Removal of pin " + x + " " + y + " is unsuccessful,\n please enter valid input.");
						}
					} else if (parsed[0].equals("post")) {

						if (parsed[6].equals("default")) {
							parsed[6] = colours[0];
						}
						if (checkColour(parsed[6]) == false) {
							out.println("Please enter a valid colour.");
						} else if (Integer.parseInt(parsed[1]) < 0 || Integer.parseInt(parsed[2]) < 0
								|| Integer.parseInt(parsed[1]) + Integer.parseInt(parsed[4]) > board.width
								|| Integer.parseInt(parsed[2]) + Integer.parseInt(parsed[3]) > board.height) {
							out.println("Please stay within board boundaries.");
						} else {
							createPost(parsed);
							out.println("Note created successfully");
						}

					} else if (parsed[0].equals("getPins")) {
						out.println(getPins(board.pins));
					} else if (parsed[0].equals("get")) {
						String message = "Notes return based upon input:@@";
						// print out all the notes, nothing is entered in the other
						// get parameters so we return everything
						if (parsed.length == 1) {
							int size = board.notes.size();
							for (int i = 0; i < size; i++) {
								int f = i + 1;
								message = message + "Note " + f + "@@";
								message = message + "Colour - " + board.notes.get(i).colour + "@@";
								message = message + "Content - " + board.notes.get(i).content + "@@";
							}
						}
						// fetch only content
						else if (parsed[1].equals("c")) {
							for (int i = 0; i < board.notes.size(); i++) {
								boolean isGood = getColour(parsed[2], board.notes.get(i).colour);
								if (isGood) {
									int f = i + 1;
									message = message + "Note " + f + "@@";
									message = message + "Colour - " + board.notes.get(i).colour + "@@";
									message = message + "Content - " + board.notes.get(i).content + "@@";
								}
							}
						}
						// fetch only data
						else if (parsed[1].equals("d")) {
							for (int i = 0; i < board.notes.size(); i++) {
								boolean isGood = getData(parsed[2], board.notes.get(i));
								if (isGood) {
									int f = i + 1;
									message = message + "Note " + f + "@@";
									message = message + "Colour - " + board.notes.get(i).colour + "@@";
									message = message + "Content - " + board.notes.get(i).content + "@@";
								}
							}
						}
						// fetch only refersTo
						else if (parsed[1].equals("r")) {
							for (int i = 0; i < board.notes.size(); i++) {
								boolean isGood = getContains(board.notes.get(i).content, parsed[2]);
								if (isGood) {
									int f = i + 1;
									message = message + "Note " + f + "@@";
									message = message + "Colour - " + board.notes.get(i).colour + "@@";
									message = message + "Content - " + board.notes.get(i).content + "@@";
								}
							}
						}
						// fetch content and data
						else if (parsed[1].equals("cd")) {
							for (int i = 0; i < board.notes.size(); i++) {
								boolean isGood = getColour(parsed[2], board.notes.get(i).colour);
								if (isGood) {
									boolean isStill = getData(parsed[3], board.notes.get(i));
									if (isStill) {
										int f = i + 1;
										message = message + "Note " + f + "@@";
										message = message + "Colour - " + board.notes.get(i).colour + "@@";
										message = message + "Content - " + board.notes.get(i).content + "@@";
									}
								}
							}
						}
						// fetch content and refersTo
						else if (parsed[1].equals("cr")) {
							for (int i = 0; i < board.notes.size(); i++) {
								boolean isGood = getColour(parsed[2], board.notes.get(i).colour);
								if (isGood) {
									boolean isStill = getContains(board.notes.get(i).content, parsed[3]);
									if (isStill) {
										int f = i + 1;
										message = message + "Note " + f + "@@";
										message = message + "Colour - " + board.notes.get(i).colour + "@@";
										message = message + "Content - " + board.notes.get(i).content + "@@";
									}
								}
							}
						}
						// fetch data and refersTo
						else if (parsed[1].equals("dr")) {
							for (int i = 0; i < board.notes.size(); i++) {
								boolean isGood = getData(parsed[2], board.notes.get(i));
								if (isGood) {
									boolean isStill = getContains(board.notes.get(i).content, parsed[3]);
									if (isStill) {
										int f = i + 1;
										message = message + "Note " + f + "@@";
										message = message + "Colour - " + board.notes.get(i).colour + "@@";
										message = message + "Content - " + board.notes.get(i).content + "@@";
									}
								}
							}

						}
						// fetch everything relating to the content, data and refersTo
						else if (parsed[1].equals("cdr")) {
							for (int i = 0; i < board.notes.size(); i++) {
								boolean isGood = getColour(parsed[2], board.notes.get(i).colour);
								if (isGood) {
									boolean isStill = getData(parsed[3], board.notes.get(i));
									if (isStill) {
										boolean isStillGood = getContains(board.notes.get(i).content, parsed[4]);
										if (isStillGood) {
											int f = i + 1;
											message = message + "Note " + f + "@@";
											message = message + "Colour - " + board.notes.get(i).colour + "@@";
											message = message + "Content - " + board.notes.get(i).content + "@@";
										}
									}
								}
							}

						}

						out.println(message);

					} else if (parsed[0].equals("clear")) {
						out.println(clear(board.notes));
					}
					else {
						out.println("Bye!");
					}

				}
			} catch (IOException e) {
				// put print error statement
				System.out.println(e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}

		private String clear(ArrayList<Note> notes) {
			String message = "";
			if (notes.size() > 0) {
				int size = notes.size();
				for (int i = size - 1; i >= 0; i--) {
					if (notes.get(i).isPinned == false) {
						int f = i + 1;
						message = message + "Note " + f + "@@";
						message = message + "Colour - " + notes.get(i).colour + "@@";
						message = message + "Content - " + notes.get(i).content + "@@";
						notes.remove(i);
					}
				}
			}
			return message;
		}

		private String getPins(ArrayList<Pin> pins) {
			String allPins = "";
			for (int i = 0; i < pins.size(); i++) {
				allPins = allPins + "X: " + board.pins.get(i).x + " Y: " + board.pins.get(i).y + ",";
			}
			return allPins;
		}

		private boolean checkColour(String colour) {
			boolean result = false;
			for (int i = 0; i < colours.length; i++) {
				if (colour.toLowerCase().equals(colours[i].toLowerCase())) {
					result = true;
				}
			}
			return result;
		}

		// returns 1 if it is removed
		// else 0
		private int removePin(Pin p, ArrayList<Pin> pins) {
			int size = pins.size();
			for (int i = 0; i < size; i++) {
				if (pins.get(i).x == p.x) {
					if (pins.get(i).y == p.y) {
						pins.remove(i);
						return 1;
					}
				}
			}
			return 0;
		}

		// updates the notes pin status
		private void updateUnpin(Pin p, ArrayList<Note> notes) {
//			int size = notes.size();
//			for (int i = size - 1; i >= 0; i--) {
//				if (notes.get(i).points.get(i).x == p.x && notes.get(i).points.get(i).y == p.y) {
//					notes.get(i).points.remove(i);
//					// if after removing that pin, if there are no
//					// pins left then it is not pinned; set to false
//					if (notes.get(i).points.size() == 0) {
//						notes.get(i).isPinned = false;
//					}
//				}
//			}
			
			int size = notes.size();
			for (int i = 0; i < size; i++) {
				for(int j = 0; j < notes.get(i).points.size(); j++) {
					if (notes.get(i).points.get(j).x == p.x && notes.get(i).points.get(j).y == p.y) {
						notes.get(i).points.remove(j);
						// if after removing that pin, if there are no
						// pins left then it is not pinned; set to false
						if (notes.get(i).points.size() == 0) {
							notes.get(i).isPinned = false;
						}
					}
				}
			}
		}

		// updates notes when a new pin has been added to the board
		private void updateNotes(Pin pin, ArrayList<Note> notes) {
			int noteSize = notes.size();
			for (int i = 0; i < noteSize; i++) {
				if (notes.get(i).point.x <= pin.x && notes.get(i).point.x + notes.get(i).width >= pin.x) {
					if (notes.get(i).point.y <= pin.y && notes.get(i).point.y + notes.get(i).height >= pin.y) {
						notes.get(i).isPinned = true;
						notes.get(i).points.add(pin);
					}
				}
			}
		}

		// tells us whether the pin is on the board
		// we must know this before adding the pin to the board object
		private boolean isPinOnBoard(Pin pin) {
			boolean on;
			if (pin.x > width || pin.y > height || pin.x < 0 || pin.y < 0) {
				on = false;
			} else {
				on = true;
			}
			return on;
		}

		private void createPost(String[] values) {
			Note newNote = new Note(values[5], values[6], Integer.parseInt(values[1]), Integer.parseInt(values[2]),
					Integer.parseInt(values[4]), Integer.parseInt(values[3]));
			board.notes.add(newNote);
		}

		private boolean getColour(String parsed, String colour) {
			if (checkColour(parsed)) {
				// if the note matches the colour the add it to the string
				if (colour.equals(parsed)) {
					return true;
				}
			}
			return false;
		}

		private boolean getContains(String compareThis, String toThat) {
			if (compareThis.contains(toThat)) {
				return true;
			} else {
				return false;
			}
		}

		private boolean getData(String parsed, Note note) {
			String[] data = parsed.split(" ");
			if (data.length != 2) {
				return false;
			} else {
				if (!isInteger(data[0]) || !isInteger(data[1])) {
					return false;
				}
				// proper input
				else {
					int x = Integer.parseInt(data[0]);
					int y = Integer.parseInt(data[1]);
					if (note.point.x <= x && note.point.x + note.width >= x) {
						if (note.point.y <= y && note.point.y + note.height >= y) {
							return true;
						}
					}

				}
			}
			return false;
		}

		public boolean isInteger(String num) {
			try {
				Integer.parseInt(num);
				return true;
			} catch (Exception e) {
				return false;
			}
		}

	}
}