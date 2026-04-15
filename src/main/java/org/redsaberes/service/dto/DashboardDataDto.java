package org.redsaberes.service.dto;

import java.util.List;
import java.util.Map;

public class DashboardDataDto {

    private final Map<String, Object> dashboardStats;
    private final List<Map<String, Object>> userCourses;
    private final List<Map<String, Object>> recentActivity;

    public DashboardDataDto(Map<String, Object> dashboardStats,
                            List<Map<String, Object>> userCourses,
                            List<Map<String, Object>> recentActivity) {
        this.dashboardStats = dashboardStats;
        this.userCourses = userCourses;
        this.recentActivity = recentActivity;
    }

    public Map<String, Object> getDashboardStats() {
        return dashboardStats;
    }

    public List<Map<String, Object>> getUserCourses() {
        return userCourses;
    }

    public List<Map<String, Object>> getRecentActivity() {
        return recentActivity;
    }
}

