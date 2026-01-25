import React from 'react';

export default function RecentTab({ recent }) {
  const sortedRecent = [...recent].sort((a, b) => 
    b.hitsInLast5Min - a.hitsInLast5Min
  );

  const thStyle = {
    padding: '10px',
    textAlign: 'left',
    fontSize: '11px',
    color: '#0a0',
    fontWeight: 'normal'
  };

  const tdStyle = {
    padding: '8px 10px',
    fontSize: '12px',
    color: '#0f0'
  };

  return (
    <div>
      <h3 style={{ marginTop: 0, fontSize: '14px', color: '#0f0' }}>
        LAST 5 MINUTES
      </h3>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ borderBottom: '1px solid #0f0' }}>
            <th style={thStyle}>METHOD</th>
            <th style={thStyle}>PATH</th>
            <th style={thStyle}>CONTROLLER</th>
            <th style={{ ...thStyle, textAlign: 'right' }}>HITS</th>
            <th style={{ ...thStyle, textAlign: 'right' }}>STATUS</th>
          </tr>
        </thead>
        <tbody>
          {sortedRecent.map((ep, idx) => {
            const isActive = ep.hitsInLast5Min > 0;
            return (
              <tr key={idx} style={{ borderBottom: '1px solid #0a0a0a' }}>
                <td style={tdStyle}>{ep.method || 'ANY'}</td>
                <td style={tdStyle}>{ep.path}</td>
                <td style={tdStyle}>{ep.controller}</td>
                <td style={{ ...tdStyle, textAlign: 'right' }}>
                  {ep.hitsInLast5Min}
                </td>
                <td style={{ 
                  ...tdStyle, 
                  textAlign: 'right', 
                  color: isActive ? '#0f0' : '#555' 
                }}>
                  {isActive ? 'ACTIVE' : 'IDLE'}
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}