import React, { useState } from 'react';
import ControllerRow from '../Controllerrow';
import { calculateControllerStats } from '../services/StatsCalculator'

export default function OverviewTab({ endpoints, metrics }) {
  const [expandedControllers, setExpandedControllers] = useState({});

  const toggleController = (controller) => {
    setExpandedControllers(prev => ({
      ...prev,
      [controller]: !prev[controller]
    }));
  };

  const controllerStats = calculateControllerStats(metrics);
  const sortedControllers = Object.entries(controllerStats)
    .sort(([, a], [, b]) => b.total - a.total);

  return (
    <div>
      <h3 style={{ marginTop: 0, fontSize: '14px', color: '#0f0' }}>
        CONTROLLERS
      </h3>
      <div style={{ border: '1px solid #0f0' }}>
        {sortedControllers.map(([name, stats]) => (
          <ControllerRow
            key={name}
            name={name}
            stats={stats}
            isExpanded={expandedControllers[name]}
            onToggle={() => toggleController(name)}
          />
        ))}
      </div>
    </div>
  );
}