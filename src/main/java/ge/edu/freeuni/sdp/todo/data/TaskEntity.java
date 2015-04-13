package ge.edu.freeuni.sdp.todo.data;

import ge.edu.freeuni.sdp.todo.core.TaskDo;

import com.microsoft.azure.storage.table.TableServiceEntity;

public class TaskEntity extends TableServiceEntity {
	
	public TaskEntity() {
	}

	private TaskEntity(TaskDo taskDo) {
		TaskEntityId id = new TaskEntityId(taskDo.getId());
		this.partitionKey = id.getPartitionKey();
		this.rowKey = id.getRowKey();
		this.text = taskDo.getText();
	}
	
	public static TaskEntity fromDo(TaskDo taskDo) {
		return new TaskEntity(taskDo);
	}
	
	public TaskDo toDo() {
		String id = getEntityId().getId();
		TaskDo taskDo = new TaskDo();
		taskDo.setId(id);
		taskDo.setText(this.getText());
		return taskDo;
	}
	
	private String text;
	
	private TaskEntityId getEntityId() {
		return new TaskEntityId(this.partitionKey, this.rowKey);
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}