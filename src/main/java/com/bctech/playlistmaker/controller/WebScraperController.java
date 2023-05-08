package com.bctech.playlistmaker.controller;

import com.bctech.playlistmaker.service.ScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class WebScraperController {
    
        private final ScraperService scraperService;


        //    Method that adds form input to the database and redirects to viewPost method handler
        @PostMapping("/scrape")
        public String scrapeSite(@ModelAttribute TaskDto task, HttpSession session) {
            taskService.saveTask(task, user.getId());
            return "redirect:/viewTasks";
        }


        //    Method handler retrieves all data from table and adds it to a model
        @GetMapping("/viewTasks")
        public String viewAllPost( RedirectAttributes redirectAttributes, Model model, HttpSession session){
            UserDto user = (UserDto) session.getAttribute("currentUser");
            if (user == null) {
                return "redirect:/login";
            }
            try {

                List<TaskDto>  allTasks = taskService.getAllTasksOfUser(user);
                model.addAttribute("allTasks", allTasks);
                return "tasks";
            }
            catch (Exception e){
                redirectAttributes.addFlashAttribute("message", e.getMessage());
                model.addAttribute("message", e.getMessage());
                return "redirect:/home";
            }
        }