package org.example.trucksy.Repository;

import org.example.trucksy.Model.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashboardRepository extends JpaRepository<Dashboard, Integer> {
}
