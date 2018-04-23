/************************************************************************************
 *	Author 		: Corey Koelewyn <Corey.koelewyn@gmail.com>							*
 *	StudentID	: V00869081															*
 *	Last update : 6/17/2017															*
 *	Class		: CSC 115															*
 *	Assignment	: 02																*
 *	Description	: This program implements a TaskList interface using Linked Lists	*
 *																					*
 *	Code for Testing, and interface Written By Mike Zastre (zastre@uvic.ca)			*
 ************************************************************************************
 */
 

public class TaskListLL implements TaskList {
    private int count = 0;
	private TaskListNode first = new TaskListNode(null); 	//identifier for start of the list
	
	public TaskListLL() {									//constructor for the TaskListLL class
    }

    public int getLength() {								//returns length of the list
		return count;
    }

    public boolean isEmpty() {								//returns true if list is empty
		return(count == 0);
    }

    public Task removeHead() {		//returns the content of first node in the list, and removes it.
        if(count == 0){				//returns null if the list is empty
			return null;
		}
		Task result = first.task;
		first = first.next;
		count--;
		return result;
    }
    
    public Task remove(int number) {	//returns the task with the number passed into the method, and removes it from the list
		if(count == 0){
			return null;
		}
		if(first.task.getNumber() == number){	//if the number specified is the first node, we use the remove head method
			return removeHead();
		}
		TaskListNode prev = first;
		for(TaskListNode tmpTask = first; tmpTask != null; tmpTask = tmpTask.next){			// Walks the taskList from First to the end
			if(tmpTask.task.getNumber() == number){
				Task toReturn = tmpTask.task; 												//creates a new pointer to the task we want to return
				prev.next = tmpTask.next;													//points the previous node at the node after the one we want to return
				count--;
				return toReturn;
			}
			prev = tmpTask;
		}
        return null;
    }

/**********Used this to troubleshoot the simulations, Added it to the interface, but commented out so it runs in the marking Scheme...
	public void printList(){
		if(count == 0){
			return;
		}
		System.out.println("size: " + getLength());
			for(TaskListNode tmpTask = first; tmpTask != null; tmpTask = tmpTask.next){
				System.out.print("pr:" + tmpTask.task.getPriority() + " num:" + tmpTask.task.getNumber() + "|");	
			}
			System.out.println("");
			return;
		
	}
*/	

    public boolean insert(int priority, int number) {		//inserts a number as per the instructions in the interface
		Task newTask = new Task(priority, number);
		TaskListNode newNode = new TaskListNode(newTask);
        if(count == 0){
			first = newNode;
			count++;
			return true;
		}else{
			for(TaskListNode tmpTask = first; tmpTask != null; tmpTask = tmpTask.next){
				if((tmpTask.task.getNumber() == number)){						// Does not insert the new number if it already exists in the node.
					return false;	
				}
			}
			TaskListNode walkList = first;										// Inserts at head if new task priority is greator than current heads priority
				if (priority > first.task.getPriority()){
					newNode.next = first;
					first = newNode;
					count++;
					return true;
				}
			for(TaskListNode tmpList = first; tmpList != null; tmpList = tmpList.next){

				if ((tmpList.next == null)){									//Inserts at the end if the last priority is greater than the new task priority
					newNode.next = tmpList.next;
					tmpList.next = newNode;
					count++;
					return true;
				}
				if(tmpList.next.task.getPriority() < priority){					//inserts priority in the correct location if it is neither the biggest, or the smallest.
					newNode.next = tmpList.next;
					tmpList.next = newNode;
					count++;
					return true;
				}
			}
		}
        return false;
    }

    public Task retrieve(int pos) {			//returns the task in the position specified, but does not remove it.
		if(isEmpty()){
			return null;
		}
		if(pos == 0){						
			return first.task;
		}
		TaskListNode tmpNode = first;
		for(int i = 0; i < pos; i++){
			if(tmpNode.next == null){
				return null;
			}
			tmpNode = tmpNode.next;
			
		}
		return tmpNode.task;		
    }
}