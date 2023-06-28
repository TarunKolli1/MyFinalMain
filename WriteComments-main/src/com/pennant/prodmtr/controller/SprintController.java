package com.pennant.prodmtr.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.pennant.prodmtr.model.Dto.FunctionalUnitdto;
import com.pennant.prodmtr.model.Dto.ModuleDTO;
import com.pennant.prodmtr.model.Dto.ProjectDto;
import com.pennant.prodmtr.model.Dto.TaskDto;
import com.pennant.prodmtr.model.Entity.FunctionalUnit;
import com.pennant.prodmtr.model.Entity.Sprint;
import com.pennant.prodmtr.model.Entity.SprintResource;
import com.pennant.prodmtr.model.Entity.SprintTasks;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.model.Entity.User;
import com.pennant.prodmtr.model.Input.SprintInput;
import com.pennant.prodmtr.model.Input.SprintResourceInput;
import com.pennant.prodmtr.model.Input.SprintTasksInput;
import com.pennant.prodmtr.model.Input.TaskInput;
import com.pennant.prodmtr.model.view.FunctionalTask;
import com.pennant.prodmtr.service.Interface.ModuleService;
import com.pennant.prodmtr.service.Interface.ProjectService;
import com.pennant.prodmtr.service.Interface.SprintService;
import com.pennant.prodmtr.service.Interface.TaskService;

@Controller
public class SprintController {

	SprintService sprintService;
	ProjectService projectService;
	ModuleService moduleService;
	TaskService taskService;
	static int sprintid = 0;

	@Autowired
	public SprintController(SprintService sprintService, ProjectService projectService, ModuleService moduleService,
			TaskService taskService) {
		super();
		this.sprintService = sprintService;
		this.projectService = projectService;
		this.moduleService = moduleService;
		this.taskService = taskService;

	}

	/**
	 * Handles the creation of a new task and displays the functional units for selection.
	 * 
	 * @param sprintInput         the input containing sprint data
	 * @param SprintResourceInput the input containing sprint resource data
	 * @param model               the model to be populated with data
	 * @return the view name for displaying functional units
	 * @throws ParseException if there is an error in parsing the input
	 */
	@RequestMapping(value = "/ShowFunctionalUnits", method = RequestMethod.POST)
	public String createTask(@Validated SprintInput sprintInput,
			@ModelAttribute SprintResourceInput SprintResourceInput, Model model) throws ParseException {
		Sprint s = sprintService.storeSprint(sprintInput.toEntity());
		SprintResource sr = SprintResourceInput.toEntity();
		sprintid = s.getSprintId();
		sr.setSprintId(s.getSprintId());
		sprintService.storeSprintResource(sr);

		List<FunctionalUnit> flist = sprintService.getFunctionalUnitsByModId(sprintInput.getModuleId());
		List<FunctionalUnitdto> funlistDto = new ArrayList<>();

		for (FunctionalUnit functionalUnit : flist) {
			FunctionalUnitdto funUnitDto = FunctionalUnitdto.fromEntity(functionalUnit);
			funlistDto.add(funUnitDto);
		}

		model.addAttribute("funlist", funlistDto);
		model.addAttribute("pro_id", sprintInput.getProjectId());
		return "ShowFunctionalUnits";
	}

	/**
	 * Retrieves and displays the functional units based on the module ID.
	 * 
	 * @param modid  the ID of the module
	 * @param prodid the ID of the project
	 * @param model  the model to be populated with data
	 * @return the view name for displaying functional units
	 * @throws ParseException if there is an error in parsing the input
	 */
	@RequestMapping(value = "/ShowFunUnits", method = RequestMethod.POST)
	public String showFunctionalUnits(@RequestParam("modid") int modid, @RequestParam("prodid") int prodid, Model model)
			throws ParseException {
		List<FunctionalUnit> flist = sprintService.getFunctionalUnitsByModId(modid);
		List<FunctionalUnitdto> funlistDto = new ArrayList<>();

		for (FunctionalUnit functionalUnit : flist) {
			FunctionalUnitdto funUnitDto = FunctionalUnitdto.fromEntity(functionalUnit);
			funlistDto.add(funUnitDto);
		}
		model.addAttribute("funlist", funlistDto);
		model.addAttribute("pro_id", prodid);
		return "ShowFunctionalUnits";
	}

	/**
	 * Retrieves and displays all sprints.
	 * 
	 * @param model the model to be populated with data
	 * @return the view name for displaying all sprints
	 */
	@RequestMapping(value = "/sprint", method = RequestMethod.GET)
	public String sprint(Model model) {
		List<Sprint> allSprints = sprintService.getAllSprints();
		model.addAttribute("allSprints", allSprints);
		return "sprint_home";
	}

	/**
	 * Retrieves and displays the details of a sprint.
	 * 
	 * @param model    the model to be populated with data
	 * @param sprintId the ID of the sprint
	 * @return the view name for displaying the sprint details
	 */
	@RequestMapping(value = "/sprint_details", method = RequestMethod.GET)
	public String getSprintDetails(Model model, @RequestParam int sprintId) {
		Sprint sprint = sprintService.getSprintDetails(sprintId);
		model.addAttribute("sprint", sprint);
		Sprint s = new Sprint();
		s.setSprintId(sprintId);
		List<SprintTasks> tasksByIdSprints = sprintService.getAllTasksBySprintId(s);
		model.addAttribute("tasksByIdSprints", tasksByIdSprints);
		return "sprint_details";
	}

	/**
	 * Displays the form for adding a new sprint.
	 * 
	 * @param model the model to be populated with data
	 * @return the view name for adding a new sprint
	 */
	@RequestMapping(value = "/add_sprint", method = RequestMethod.GET)
	public String addSprint(Model model) {
		List<ProjectDto> pl = projectService.getAllProjects();
		model.addAttribute("projects", pl);
		List<User> lu = sprintService.getAllUsers();
		model.addAttribute("users", lu);
		return "add_sprint";
	}

	/**
	 * Displays the functional unit form.
	 * 
	 * @return the view name for adding a new functional unit
	 */
	@RequestMapping(value = "/FunctionalUnit", method = RequestMethod.GET)
	public String addSprint() {
		return "FunctionalUnit";
	}

	/**
	 * Displays the subtask details.
	 * 
	 * @return the view name for subtask details
	 */
	@RequestMapping(value = "/SubTaskdetails", method = RequestMethod.GET)
	public String SubtaskDetails() {
		return "SubtaskDetails";
	}

	/**
	 * Displays the form for creating a subtask.
	 * 
	 * @return the view name for creating a subtask
	 */
	@RequestMapping(value = "/CreateSubTask", method = RequestMethod.GET)
	public String CreateSubtask() {
		return "CreateSubtask";
	}

	/**
	 * Displays the backlog tasks.
	 * 
	 * @param model the model to be populated with data
	 * @return the view name for backlog tasks
	 */
	@RequestMapping(value = "/backlogs", method = RequestMethod.GET)
	public String pastdue(Model model) {
		ArrayList<Sprint> SprintList = (ArrayList<Sprint>) sprintService.getBacklogs();
		model.addAttribute("sprintList", SprintList);
		return "backlog";
	}

	/**
	 * Retrieves and displays the backlog tasks for a sprint.
	 * 
	 * @param model      the model to be populated with data
	 * @param sprnModlId the ID of the sprint module
	 * @param sprnId     the ID of the sprint
	 * @return the view name for displaying backlog tasks
	 */
	@RequestMapping(value = "/BacklogTasks", method = RequestMethod.GET)
	public String getBacklogTasks(Model model, @RequestParam("sprnModlId") int sprnModlId,
			@RequestParam("sprnId") int sprnId) {
		Sprint sprint = sprintService.getSprintDetails(sprnId);
		List<Task> taskList = sprintService.getTasks(sprnModlId);
		model.addAttribute("sprint", sprint);
		model.addAttribute("taskList", taskList);
		return "BacklogTasks";
	}

	/**
	 * Retrieves a module by ID and returns it as JSON.
	 * 
	 * @param projectId the ID of the project
	 * @return the JSON representation of the module
	 */
	@ResponseBody
	@RequestMapping(value = "/getModuleById", method = RequestMethod.POST, produces = "application/json")
	public String getModuleById(@RequestParam("projectId") int projectId) {
		List<ModuleDTO> moduleList = sprintService.getSprintModulesByProjectId(projectId);
		Gson gson = new Gson();
		String json = gson.toJson(moduleList);
		return json;
	}

	/**
	 * Displays the form for creating a task.
	 * 
	 * @param ft    the functional task
	 * @param model the model to be populated with data
	 * @return the view name for creating a task
	 */
	@RequestMapping(value = "/Task", method = RequestMethod.POST)
	public String createTask(@ModelAttribute FunctionalTask ft, Model model) {
		model.addAttribute("funtask", ft);
		List<User> lu = sprintService.getAllUsers();
		model.addAttribute("users", lu);
		List<TaskDto> tasks = taskService.getAllTasks();
		model.addAttribute("tasks", tasks);
		return "Task";
	}

	/**
	 * Handles the submission of the task form and adds the task.
	 * 
	 * @param taskInput        the task input data
	 * @param sprintTasksInput the sprint tasks input data
	 * @param model            the model to be populated with data
	 * @return the view name for task added
	 */
	@RequestMapping(value = "/TaskAdded", method = RequestMethod.POST)
	public String TaskAdded(@ModelAttribute TaskInput taskInput, @ModelAttribute SprintTasksInput sprintTasksInput,
			Model model) {
		Task t = sprintService.storeTask(taskInput.toEntity());
		sprintTasksInput.setSprintId(sprintid);
		sprintTasksInput.setTaskId(t.getTaskId());
		sprintTasksInput.setUserId(t.getTaskSupervisor().getUserId());
		SprintTasks st;
		st = sprintTasksInput.toEntity();
		sprintService.storeSprintTasks(st);
		return "TaskAdded";
	}

	/**
	 * Retrieves and displays the sprint details for a project.
	 * 
	 * @param projectId the ID of the project
	 * @param model     the model to be populated with data
	 * @return the view name for displaying the sprint details
	 */
	@RequestMapping(value = "/sprintsByProject", method = RequestMethod.GET)
	public String sprintByProject(@RequestParam("projectId") int projectId, Model model) {
		List<Sprint> sprintsByProject = sprintService.getSprintsByProjId(projectId);
		model.addAttribute("sprintsByProject", sprintsByProject);
		return "sprint_by_project";
	}
}
