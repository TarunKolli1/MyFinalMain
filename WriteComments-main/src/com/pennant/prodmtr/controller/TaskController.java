package com.pennant.prodmtr.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.pennant.prodmtr.model.Dto.TFilterCriteria;
import com.pennant.prodmtr.model.Dto.TaskDto;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.model.view.TaskUpdateFormModel;
import com.pennant.prodmtr.service.Interface.TaskService;

@Controller
public class TaskController {

	private final TaskService taskService;

	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	/**
	 * Retrieves and displays tasks for a specific user.
	 * 
	 * @param model the model to be populated with data
	 * @return the view name for the task list
	 */
	@RequestMapping(value = "/tasksbyid", method = RequestMethod.GET)
	public String viewTasksForUser(Model model) {
		// Use the userId to fetch the tasks
		int userId = 1; // Replace with actual userId
		List<TaskDto> tasks = taskService.getTasksByUserId(userId);

		// Add the tasks to the model
		model.addAttribute("tasks", tasks);

		// Return the view name
		return "Taskslist"; // Replace with actual view name
	}

	/**
	 * Retrieves and displays all tasks.
	 * 
	 * @param model the model to be populated with data
	 * @return the view name for the task list
	 */
	@RequestMapping(value = "/tasks", method = RequestMethod.GET)
	public String viewAllTasks(Model model) {
		// Use the userId to fetch the tasks
		// Replace with actual userId
		List<TaskDto> tasks = taskService.getAllTasks();

		// Add the tasks to the model
		model.addAttribute("tasks", tasks);

		// Return the view name
		return "Taskslist"; // Replace with actual view name
	}

	/**
	 * Retrieves and displays details for a specific task.
	 * 
	 * @param taskId the ID of the task
	 * @param model  the model to be populated with data
	 * @return the view name for the task details
	 */
	@RequestMapping(value = "/taskdetailsbyid", method = RequestMethod.GET)
	public String getAllTasks(@RequestParam("taskId") int taskId, Model model) {

		List<TaskDto> tasks = taskService.getTasksByUserId(taskId);

		model.addAttribute("tasks", tasks);

		return "taskdetailsbyid";
	}

	/**
	 * Updates the status of a task.
	 * 
	 * @param taskId the ID of the task to update
	 * @param model  the model to be populated with data
	 * @return the view name for the task list
	 */
	@RequestMapping(value = "/updateTaskStatus", method = RequestMethod.POST)
	public String updateTaskStatus(@RequestParam("taskId") int taskId, Model model) {
		// Retrieve the existing task from the database using the task ID
		Task task = taskService.getTaskById(taskId);
		model.addAttribute("task", task);
		return "Taskslist";
	}

	/**
	 * Handles the successful update of a task status.
	 * 
	 * @param taskId the ID of the task that was updated
	 * @return the redirect path to the task list page
	 */
	@RequestMapping(value = "/updateSuccess", method = RequestMethod.POST)
	public String updateTaskStatusSuccess(@RequestParam("taskId") int taskId) {

		// Retrieve the existing task from the database using the task ID
		Boolean task = taskService.updateStatus(taskId);

		// Update the task status

		// Redirect to the task list page or show a success message
		return "redirect:/tasks";
	}

	/**
	 * Filters tasks based on provided criteria.
	 * 
	 * @param filterCriteria the criteria for filtering tasks
	 * @param bindingResult  the result of the validation
	 * @return the JSON response containing the filtered tasks
	 */
	@PostMapping(value = "/Taskfilter", produces = "application/json")
	@ResponseBody
	public String PtfilterTasks(@Validated TFilterCriteria filterCriteria, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			// Handle validation errors
			// Return appropriate error response
			return "Validation Error";
		}
		System.out.println(filterCriteria.getTaskSupervisorId());
		List<TaskDto> filteredTasks = taskService.PtfilterTasks(filterCriteria);

		Gson gson = new Gson();
		String json = gson.toJson(filteredTasks);

		// Return the JSON response
		return json;
	}

	/**
	 * Retrieves and displays individual tasks for a project.
	 * 
	 * @param projId the ID of the project
	 * @param model  the model to be populated with data
	 * @return the view name for the individual tasks
	 */
	@RequestMapping(value = "/Indvtasks", method = RequestMethod.GET)
	public String viewIndvtasks(@RequestParam("projId") Integer projId, Model model) {
		List<Task> tasks = taskService.getTasksByProjectId(projId);
		model.addAttribute("tasks", tasks);
		return "Indvtasks";
	}

	/**
	 * Sets the status of a task.
	 * 
	 * @param taskId  the ID of the task to set the status for
	 * @param model   the model to be populated with data
	 * @param session the HttpSession for accessing session attributes
	 * @return the view name for updating the task status
	 */
	@RequestMapping(value = "/setTaskStatus", method = RequestMethod.GET)
	public String setTaskStatus(@RequestParam int taskId, Model model, HttpSession session) {
		System.out.println("here in setTaskStatus");
		Task task = taskService.getTaskById(taskId);
		model.addAttribute("task", task);
		return "taskStatusUpdate";
	}

	/**
	 * Sets the details of a task.
	 * 
	 * @param taskUpdateFormModel the form model containing the task details
	 * @param model               the model to be populated with data
	 * @return the redirect path to the activity page
	 */
	@RequestMapping(value = "/setTaskDetails", method = RequestMethod.GET)
	public String setTaskUpdateFormModel(@Validated TaskUpdateFormModel taskUpdateFormModel, Model model) {
		System.out.println("here in setTaskStatus");
		taskService.updateTaskStatus(taskUpdateFormModel);
		return "redirect:activity";
	}

}
