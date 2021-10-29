package com.bugtracker.backend.service;

import com.bugtracker.backend.dto.PriorityStatsDto;
import com.bugtracker.backend.entity.Priority;
import com.bugtracker.backend.entity.Project;
import com.bugtracker.backend.repository.ProjectRepository;
import com.bugtracker.backend.util.GlobalUtil;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PriorityService priorityService;

    public Optional<Project> saveProject(Project project, int priorityId){
        Priority priority = priorityService.getPriorityById(priorityId).orElse(null);
        project.setProjectPriority(priority);
        return Optional.of( projectRepository.save(project));
    }

    public void addAllProjects(List<Project> projects){
        projectRepository.saveAll(projects);
    }

    public List<Project> getProjects(int pageNo, String sortBy, Integer priority){
        Pageable page = PageRequest.of(pageNo, GlobalUtil.itemsPerPage, Sort.by(Sort.Direction.DESC,sortBy));
        if(priority == null){
            return projectRepository.getAll(page);
        }else {
            return projectRepository.getAllByPriority(priority,page);
        }

    }
    public List<Project> getProjectsByLastActive(int pageNo){
        Pageable page = PageRequest.of(pageNo, GlobalUtil.itemsPerPage);
        return projectRepository.getByLastActive(page);
    }

    public List<Project> getProjects(){
        return  projectRepository.findAll();
    }


    public List<Project> getProjectsByUser(int userId,int pageNo, String sortBy, Integer priority){
        Pageable page = PageRequest.of(pageNo,GlobalUtil.itemsPerPage, Sort.by(Sort.Direction.DESC,sortBy));
        if(priority==null){

            return projectRepository.findByUser(userId,page);
        }else{

            return projectRepository.findByUserAndPriority(userId,priority,page);
        }

    }

    public List<Project> getProjectsByStatus(boolean status,int pageNo, String sortBy, Integer priority){
        Pageable page = PageRequest.of(pageNo,GlobalUtil.itemsPerPage, Sort.by(Sort.Direction.DESC,sortBy));
        if(priority==null){

            return projectRepository.findByStatus(status,page);
        }else{
            return projectRepository.findByStatusAndPriority(status,priority,page);
        }

    }

    @Nullable
    public Project getProjectByUser(int userId, int projectId){
        return projectRepository.findProjectByUser(userId, projectId);
    }

    public Optional<Project> getProjectById(@NonNull int id){
        return projectRepository.findById(id);
    }

    public List<Project> getByUserAndRole(@NonNull int userId,@NonNull int roleId,int pageNo){
        Pageable page = PageRequest.of(pageNo,GlobalUtil.itemsPerPage);
        return projectRepository.findByUserAndRole(userId,roleId,page);
    }

    public List<Project> getByUserAndPriority(@NonNull int userId, @NonNull int priorityId, int pageNo){
        Pageable page = PageRequest.of(pageNo,GlobalUtil.itemsPerPage);
        return projectRepository.findByUserAndPriority(userId,priorityId,page);
    }

    public List<Project> getByPriority(@NonNull int priorityId, int pageNo){
        Pageable page = PageRequest.of(pageNo,GlobalUtil.itemsPerPage);
        return projectRepository.findByPriority(priorityId);
    }

    public String deleteProject(int id){
        projectRepository.deleteById(id);
        return "Project " + id + " deleted";
    }

    public Optional<Project> updateProject(Project project,int projectId, int priorityId){
        log.info("New project title: "+project.getProjectName()+" New project desc: "+project.getProjectDescription()+" new project date:"+project.getDateStarted().toString());
        Project existingProject = projectRepository.findById(projectId).orElse(null);
        Priority priority = priorityService.getPriorityById(priorityId).orElse(null);
        existingProject.setProjectName(project.getProjectName());
        existingProject.setProjectDescription(project.getProjectDescription());
        existingProject.setProjectOpen(project.isProjectOpen());
        existingProject.setProjectPriority(priority);

        return Optional.of( projectRepository.save(existingProject));
    }

    public int[] getNoOfActiveAndClosedAndAssignedTicketsOfProject(@NonNull int projectId,@NonNull int userId){
        List<Integer> result= new ArrayList<>();
        result.add(projectRepository.getNumberOfActiveTicketsForProject(projectId));
        result.add(projectRepository.getNumberOfClosedTicketsForProject(projectId));
        result.add(projectRepository.getNumberOfAssignedTicketsOfProjectToUser(projectId,userId));
        return result.stream().mapToInt(i->i).toArray();

    }

    public int getNoOfUsersOfProject(@NonNull int projectId){
        return projectRepository.getNumberOfUsersOfProject(projectId);
    }

    public List<PriorityStatsDto> getProjectsCountByPriority(){
        List<PriorityStatsDto> results = new ArrayList<>();

        for(int i=1;i<=10;i++){
            results.add(new PriorityStatsDto("Project priority level: "+i,projectRepository.getProjectsCountByPriority(i)));
        }
        return results;
    }

    public List<PriorityStatsDto> getActiveTicketsCountForAllProjects(){
        List<PriorityStatsDto> results = new ArrayList<>();
        for(Project project : projectRepository.findAll()){
            results.add(new PriorityStatsDto(project.getProjectName(),projectRepository.getNumberOfActiveTicketsForProject(project.getId())));
        }
        return results;
    }

    public Integer getProjectsCount(){
        return projectRepository.getAllCount();
    }


}
