package com.pennant.prodmtr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pennant.prodmtr.model.Dto.FunctionalUnitdto;
import com.pennant.prodmtr.model.Input.FunctionalUnitinput;
import com.pennant.prodmtr.service.Interface.FunctionalUnitService;

@Controller
public class FunctionalUnitController {

	@Autowired
	FunctionalUnitService Funitservice;

	@Autowired
	public FunctionalUnitController(FunctionalUnitService Funitservice) {
		this.Funitservice = Funitservice;
		// TODO Auto-generated constructor stub
	}

	/**
	 * Retrieves functional units by Module ID.
	 *
	 * @param modId the ID of the module
	 * @param model the model to be populated with data
	 * @return the view name for displaying functional units
	 */
	@RequestMapping(value = "/funitsbymodlId", method = RequestMethod.GET)
	public String getModuleDetailsByProjId(@RequestParam("modId") Integer modId, Model model) {
		System.out.println("Functional unit jsp called");
		System.out.println("funitid " + modId);
		List<FunctionalUnitdto> funits = Funitservice.getFunctionalunitByModId(modId);
		System.out.println("funity data" + funits);
		model.addAttribute("funitdto", funits);
		return "funitsbymodlId";
	}

	/**
	 * Displays the form for creating a new functional unit.
	 *
	 * @return the view name for the create functional unit form
	 */
	@RequestMapping(value = "/createfunit", method = RequestMethod.GET)
	public String createnewFunit() {
		return "createfunit";
	}

	/**
	 * Creates a new functional unit based on the input data.
	 *
	 * @param Funitinput the input data for creating a functional unit
	 * @param model      the model to be populated with data
	 * @return a redirect URL to display functional units for the associated module
	 */
	@RequestMapping(value = "/createFunitsuccess", method = RequestMethod.POST)
	public String Createmodulesuccess(@Validated FunctionalUnitinput Funitinput, Model model) {
		System.out.println("createModule jsp called");
		System.out.println(Funitinput);
		Funitservice.createFunit(Funitinput);
		Integer modId = Funitinput.getModlId(); // Assuming you have a method to retrieve the module ID from the input
												// object
		return "redirect:/funitsbymodlId?modId=" + modId;
	}
}
