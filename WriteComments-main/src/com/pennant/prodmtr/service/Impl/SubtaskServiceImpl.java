package com.pennant.prodmtr.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pennant.prodmtr.Dao.Interface.SubtaskDao;
import com.pennant.prodmtr.model.Entity.Subtask;
import com.pennant.prodmtr.model.Entity.SubtaskPrimaryKey;
import com.pennant.prodmtr.model.Input.SubtaskInput;
import com.pennant.prodmtr.service.Interface.SubtaskService;

@Service
@Transactional
public class SubtaskServiceImpl implements SubtaskService {

	@Autowired
	private SubtaskDao subtaskDao;

	public void saveSubtask(Subtask subtask) {
		subtaskDao.saveSubtask(subtask);
	}

	@Override
	public void setNewSubTask(SubtaskInput subtaskInput) {
		SubtaskPrimaryKey spk = new SubtaskPrimaryKey();
		spk.setTaskId(subtaskInput.getTaskId());
		spk.setSubtaskId((subtaskInput.getSubtaskId()));
		Subtask subtask = subtaskInput.toEntity();
		subtask.setPrimaryKey(spk);
		subtask.getPrimaryKey().getTaskId();

		subtaskDao.setNewSubTask(subtask);

	}
}
