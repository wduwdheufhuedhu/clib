type IconProps = { className?: string }

export function IconCore({ className }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 80 80" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
      <g fill="none" stroke="currentColor" strokeWidth="1.4" strokeLinejoin="round">
        <path d="M40 12 L62 26 V54 L40 68 L18 54 V26 Z" />
        <path d="M40 28 L52 36 V52 L40 60 L28 52 V36 Z" opacity="0.65" />
        <path d="M40 44 V68 M40 12 V22" strokeLinecap="round" />
      </g>
    </svg>
  )
}

export function IconRelay({ className }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 80 80" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
      <g fill="none" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round">
        <circle cx="22" cy="40" r="9" />
        <circle cx="58" cy="40" r="9" />
        <circle cx="40" cy="22" r="7" opacity="0.75" />
        <circle cx="40" cy="58" r="7" opacity="0.75" />
        <path d="M31 40 H49 M40 29 V51 M31 31 L49 49 M49 31 L31 49" opacity="0.45" />
      </g>
    </svg>
  )
}

export function IconLattice({ className }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 80 80" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
      <g fill="none" stroke="currentColor" strokeWidth="1.4">
        <path d="M14 58 H66 V22 H14 Z" />
        <path d="M14 40 H66 M40 22 V58" opacity="0.55" />
        <path d="M27 31 H53 M27 49 H53" opacity="0.4" strokeDasharray="3 4" />
        <circle cx="40" cy="40" r="5" />
      </g>
    </svg>
  )
}

export function IconShield({ className }: IconProps) {
  return (
    <svg className={className} viewBox="0 0 64 64" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
      <path
        d="M32 8 L52 18 V34 C52 46 42 56 32 60 C22 56 12 46 12 34 V18 Z"
        fill="none"
        stroke="currentColor"
        strokeWidth="1.35"
        strokeLinejoin="round"
      />
      <path d="M24 34 L30 40 L42 26" fill="none" stroke="currentColor" strokeWidth="1.35" strokeLinecap="round" />
    </svg>
  )
}
