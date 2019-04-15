package CompilerCode;

import java.util.ArrayList;
/**
 * Keeps track of the values on the stack and in the registers.
 * @author Ben
 *
 */
public class RegStack {
	int regNum;
	private ArrayList<Integer> stack = new ArrayList<Integer>();
	private ArrayList<String> names = new ArrayList<String>();
	
	
	RegStack(){
		this.regNum = 14; //rsp & rbp not touched
		setup(regNum);
		//How to get associated stack value from name
		//stack.get(names.lastIndexOf("%eax"));
	}
	
	RegStack(int regNum){
		this.regNum = regNum;
		setup(regNum);
	}
	
	/**
	 * sets adds the number of free registers
	 * @param regNum
	 */
	private void setup(int regNum){
		int i = 0;
		for(i = 0; i < regNum; i++){
			stack.add(0);
		}
		//sets the stack and base to never be used for standard operations
	}
	
	/**
	 * gives user the next free reg or a stack space if no free reg exist
	 * @return
	 */
	public int giveMeReg(){
		int LRU = findLRUReg();
		if(stack.get(LRU) == 0){ //reg is free!
			stack.set(LRU, 1);
			return LRU;
		}
		else{ //LRU reg is not free :(
			//use isRegEmpty next time
			//auto pushing to stack
			return nextFreeSpace();
		}
	}
	
	/**
	 * 
	 * @return free reg or lest used reg if no free reg exist
	 */
	public int findLRUReg(){
		int holder = 0;
		for(int i = 0; i < regNum; i++){
			if(stack.get(i) == 0){ //free reg!
				return i;
			}
			if(stack.get(i) > stack.get(holder)){ //finds largest non 0 (not free) reg
				holder = i;
			}
		}
		return holder;
	}
	
	/**
	 * rename of isRegEmpty for useability
	 * @return
	 */
	public boolean isThereEmptyReg(){
		return isRegEmpty();
	}
	
	/**
	 * checks if the LRU reg is free (0) (if there is a free reg)
	 * @return
	 */
	public boolean isRegEmpty(){
		int tmp = stack.get(this.findLRUReg());
		if(tmp == 0){
			return true;
		}
		return false;
	}
	
	/**
	 * checks if reg is free (0)
	 * @param reg
	 * @return
	 */
	public boolean isRegEmpty(int reg){
		int tmp = stack.get(reg);
		if(tmp == 0){
			return true;
		}
		return false;
	}
	
	/**
	 * uses the next reg and frees the reg
	 * @param reg
	 * @return
	 */
	public int pushToStack(int reg){
		int tmp = nextFreeSpace();
		stack.set(tmp, 0);
		return tmp;
	}
	
	/**
	 * increments the LRU, sets the used register to 1, for optimization
	 * @param num
	 */
	public void usedReg(int num){
		incrementLRU();
		stack.set(num, 1);
	}
	
	/**
	 * increments the LRU, we used a register, for optimization
	 */
	private void incrementLRU(){
		int i = 0;
		for(i = 0; i < regNum; i++){
			if(stack.get(i) != 0){ //increments the used regs
				stack.set(i, stack.get(i)+1);
			}
		}
	}
	
	public int giveMeStack() {
		return nextFreeSpace();
	}
	
	/**
	 * uses the next free space
	 * @return the int in stack space of that space
	 */
	public int nextFreeSpace(){ //on stack
		for(int i = regNum; i < stack.size(); i++){
			if(stack.get(i) == 0){ //free spot already allocated stack				
				stack.set(i, 1); //marks as now used space
				return i;
			}
		}
		stack.add(1); //no already allocated stack spaces are free
		return stack.size();
	}
	
	/**
	 * give effectively rsp size
	 */
	public int stackSize(){
		return stack.size()-regNum;
	}
}



















//Misunderstood group goals for this class
//keeping as functionality is actually in this class, just alot of other crap
/** public class RegStack {
	int regNum;
	private ArrayList<Integer> stack;
	private ArrayList<Integer> lastUsed;
	
	/**enum enum_register{
		rax (0),
		rbx (1),
		rcx (2),
		rsi (3),
		rbp (4),
		rsp (5),
		r8 (6),
		r9 (),
		r10 (),
		r11 (),
		r12 (),
		r13 (),
		r14 (),
		r15 ()
	};
	
	
	RegStack(){
		this.regNum = 16;
		setup(regNum);
	}
	
	RegStack(int regNum){
		this.regNum = regNum;
		setup(regNum);
	}
	
	private void setup(int regNum){
		int i = 0;
		stack = new ArrayList<Integer>();
		lastUsed = new ArrayList<Integer>();
		for(i = 0; i < regNum; i++){
			stack.add(0);
			lastUsed.add(1);
		}
		//sets the stack and base to never be used for standard operations
		if(lastUsed.size() >= 5){
			lastUsed.set(4, 0);
			lastUsed.set(5, 0);
		}
	}
	
	/**
	 * useful for selecting a reg with setReg 
	
	public int findLRUReg(){
		int holder = 0;
		for(int i = 0; i < regNum; i++){
			if(lastUsed.get(i) < lastUsed.get(holder)){
				holder = i;
			}
		}
		return holder;
	}
	
	
	/**
	 * sets value to the register reg
	 * 
	 * @param reg
	 * @param value
	 * @return stack value if non empty (0 value) register
	
	public int setReg(int reg, int value){
		/**
		 * change reg value on AR here
		
		if(stack.get(reg) != 0){ //non empty register 
			return pushRegToStack(reg);
		}
		stack.set(reg, value);
		incrementLRU();
		lastUsed.set(reg, 1);
		return reg;
	}
	
	/**
	 * if you don't care what reg it's using
	 * @param value
	 * @return
	 
	public int setReg(int value){
		/**
		 * change reg value on AR here
		 
		return setReg(findLRUReg(), value);
	}

	/**
	 * hotfix to the "loss of a variable if pushed to the stack while trying to push to reg" problem (can't return 2 ints)
	 * @return true if the LRU reg is empty (0)
	 
	public boolean isRegEmpty(){
		int tmp = stack.get(this.findLRUReg());
		if(tmp == 0){
			return true;
		}
		return false;
	}
	
	/**
	 * hotfix to the "loss of a variable if pushed to the stack while trying to push to reg" problem (can't return 2 ints)
	 * @param reg the reg num in question if it's empty or not
	 * @return true if the given reg number is empty (0)
	 
	public boolean isRegEmpty(int reg){
		int tmp = stack.get(reg);
		if(tmp == 0){
			return true;
		}
		return false;
	}
	
	public int getReg(int reg){
		incrementLRU();
		lastUsed.set(reg, 1);
		return stack.get(reg); 
	}
	
	private void incrementLRU(){
		int i = 0;
		for(i = 0; i < regNum; i++){
			if(lastUsed.get(i) != 0){ //making sure not a protected register (rpb, rsp)
				lastUsed.set(i, lastUsed.get(i)+1);
			}
		}
	}
	
	public int pushRegToStack(int reg){
		int tmp = pushToStack(stack.get(reg));
		stack.set(reg, 0);
		return tmp;
	}
	
	/**
	 * 
	 * @param val to go onto the stack
	 * @return the location onto the stack
	 
	public int pushToStack(int val){
		int i = regNum; 
		//checks for free spots on stack
		for(i = regNum; i < lastUsed.size(); i++){
			if(lastUsed.get(i) == 0){ //free spot already allocated stack				
				lastUsed.set(i + regNum, 1); //marks as now used space
				return i - regNum;
			}
		}
			//no already allocated stack spaces are free
		stack.add(val);
		lastUsed.add(1);
		stack.set(5, stack.get(5) + 4); //increments the stack pointer by 4 (# bytes in an int)
		return(stack.size() - regNum); //new is last item in stack
	}
	
	public int cutFromStack(int offset){
		lastUsed.set(offset + regNum, 0); //marks as free space
		return stack.get(offset + regNum);
	}
	
	public int copyFromStack(int offset){
		return stack.get(offset + regNum);
	}
	
	public void delFromStack(int offset){
		lastUsed.set(offset + regNum, 0); //marks as free space
	}
	
	public int getrbp(){
		if(stack.size() >= 5){
			return 4;
		}
		return -1;
	}
	
	public int getrsp(){
		if(stack.size() >= 6){
			return 5;
		}
		return -1;
	}
	
	/** 
	 * if you want a guarantee that reg will not auto pop
	 * @param reg
	 
	public void protectReg(int reg){
		if(reg < regNum)
			lastUsed.set(reg, 0);
	}
	
	/** 
	 * allows reg to pop
	 * ::CAUTION:: can cause the base or stack pointers to be overwritten automatically
	 * @param reg
	 
	public void unprotectReg(int reg){
		if(reg < regNum)
			lastUsed.set(reg, 1);
	}*/