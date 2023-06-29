package com.pennant.prodmtr.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pennant.prodmtr.model.Input.SubtaskInput;
import com.pennant.prodmtr.service.Interface.SubtaskService;
import com.pennant.prodmtr.service.Interface.TaskService;

@Controller
public class SubtaskController {

	@Autowired
	public SubtaskService subtaskService;
	private TaskService taskService;

	// Create a logger instance for the SubtaskController class using the LoggerFactory.getLogger method.
	// This logger can be used to record and output log messages during the execution of the SubtaskController.
	// It allows for better visibility and debugging of the application by capturing important information.
	public static final Logger logger = LoggerFactory.getLogger(SubtaskController.class);

	/**
	 * Saves a new subtask.
	 * 
	 * @param subtaskInput the input data for the subtask
	 * @param model        the model to be populated with data
	 * @return the view name for the task list or error page
	 */
	@RequestMapping(value = "/saveSubtask", method = RequestMethod.GET)
	public String saveSubtask(@Validated SubtaskInput subtaskInput, Model model) {
		System.out.println("id is " + subtaskInput.getTaskId());
		try {
			logger.info("Here the try will be trying to handle the statement");

			logger.info(subtaskInput.getSubtaskDescription());
			logger.info(subtaskInput.getCreationDate());
			logger.info("The id is: {}", subtaskInput.getTaskId());
			logger.info("The subtask ID is: {}", subtaskInput.getSubtaskId());
			logger.info("The number of hours spent is :{}", subtaskInput.getNumberOfHours());
			subtaskService.setNewSubTask(subtaskInput);
			return "Taskslist";
		} catch (DataIntegrityViolationException ex) {
			// Handle the constraint violation exception
			ex.printStackTrace(); // or log the error
			model.addAttribute("error", "Constraint violation occurred. Please try again.");
			logger.info("error -> constraint violation has been occured.Please try Again ");
			logger.info(subtaskInput.getSubtaskDescription());
			logger.info(subtaskInput.getCreationDate());
			logger.info("The id is: {}", subtaskInput.getTaskId());
			logger.info("The subtask ID is: {}", subtaskInput.getSubtaskId());
			logger.info("The number of hours spent is :{}", subtaskInput.getNumberOfHours());
			logger.info("The tasklist is displayed at the end");
			return "Taskslist"; // Show an error page to the user
		}
	}

	/**
	 * Retrieves the subtask form for creating a new subtask.
	 * 
	 * @param taskId the ID of the task associated with the subtask
	 * @param model  the model to be populated with data
	 * @return the view name for creating a subtask
	 */
	@RequestMapping(value = "/createSubtask", method = RequestMethod.GET)
	public String getSubtaskForm(@RequestParam("taskId") int taskId, Model model) {
		SubtaskInput subtaskInput = new SubtaskInput();
		logger.info("The subtask input has been created");
		subtaskInput.setTaskId(taskId); // Set the task_id in the SubtaskInput object
		logger.info("The task Id set is :{}", taskId);
		model.addAttribute("subtaskInput", subtaskInput);
		logger.info("here the subtaskinput is displayed");
		model.addAttribute("taskId", taskId);
		logger.info("task id model attribute has been assigned");
		logger.info("Finally the createsubtask is called");
		return "createsubtask";
	}

	// @RequestMapping(value = "/setSubTaskStatus", method = RequestMethod.GET) public String
	// setSubTaskStatus(@RequestParam("compostiteId") String compostiteId, Model model) { Subtask subtask =
	// subtaskService.getSubTaskByCompositeId(compostiteId); // subtaskService.setSubtask return "createsubtask"; }
}
