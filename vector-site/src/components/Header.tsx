const links = [
  { href: '#work', label: 'Featured' },
  { href: '#services', label: 'Services' },
  { href: '#insights', label: 'Insights' },
  { href: '#careers', label: 'Careers' },
] as const

export function Header() {
  return (
    <header className="site-header">
      <a className="mark" href="#top">
        <span className="mark__glyph" aria-hidden>
          <svg width="22" height="22" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="1" y="1" width="30" height="30" stroke="currentColor" strokeWidth="1.5" />
            <path d="M9 22V10l7 9 7-9v12" stroke="currentColor" strokeWidth="1.5" strokeLinecap="square" />
          </svg>
        </span>
        <span className="mark__text">Vector Atelier</span>
      </a>
      <nav className="nav" aria-label="Primary">
        <ul>
          {links.map(({ href, label }) => (
            <li key={href}>
              <a href={href}>{label}</a>
            </li>
          ))}
        </ul>
      </nav>
    </header>
  )
}
