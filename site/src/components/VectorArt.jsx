/**
 * Minimal 2D vector compositions — stroke-only, no raster assets.
 */

export function HeroComposition({ className = '' }) {
  return (
    <svg
      className={className}
      viewBox="0 0 520 400"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      aria-hidden
    >
      <defs>
        <pattern
          id="grid"
          width="32"
          height="32"
          patternUnits="userSpaceOnUse"
        >
          <path
            d="M32 0H0V32"
            stroke="currentColor"
            strokeOpacity="0.12"
            strokeWidth="0.5"
          />
        </pattern>
      </defs>
      <rect width="520" height="400" fill="url(#grid)" />
      <g stroke="currentColor" strokeWidth="1" strokeLinecap="square">
        <rect x="48" y="72" width="200" height="200" opacity="0.9" />
        <rect x="120" y="120" width="200" height="200" opacity="0.55" />
        <rect x="192" y="168" width="200" height="200" opacity="0.3" />
        <line x1="48" y1="72" x2="120" y2="120" opacity="0.35" />
        <line x1="248" y1="72" x2="320" y2="120" opacity="0.35" />
        <line x1="48" y1="272" x2="120" y2="320" opacity="0.35" />
        <line x1="248" y1="272" x2="320" y2="320" opacity="0.35" />
        <circle cx="48" cy="72" r="3" fill="currentColor" />
        <circle cx="248" cy="72" r="3" fill="currentColor" />
        <circle cx="392" cy="320" r="3" fill="currentColor" />
        <circle cx="48" cy="272" r="3" fill="currentColor" />
        <path
          d="M400 48 L472 120 L400 192 L328 120 Z"
          strokeOpacity="0.45"
          fill="none"
        />
        <line x1="400" y1="48" x2="400" y2="360" strokeOpacity="0.2" />
        <line x1="72" y1="360" x2="480" y2="360" strokeOpacity="0.25" />
      </g>
    </svg>
  )
}

export function CapabilityMark({ variant = 0, className = '' }) {
  const paths = [
    <g key="0" stroke="currentColor" strokeWidth="1" fill="none">
      <rect x="8" y="12" width="56" height="40" />
      <line x1="8" y1="24" x2="64" y2="24" opacity="0.5" />
      <line x1="8" y1="32" x2="48" y2="32" opacity="0.35" />
      <circle cx="52" cy="44" r="3" fill="currentColor" />
    </g>,
    <g key="1" stroke="currentColor" strokeWidth="1" fill="none">
      <path d="M12 44 L36 16 L60 44 Z" />
      <line x1="36" y1="16" x2="36" y2="52" opacity="0.4" />
      <circle cx="36" cy="52" r="2.5" fill="currentColor" />
    </g>,
    <g key="2" stroke="currentColor" strokeWidth="1" fill="none">
      <rect x="14" y="14" width="44" height="44" transform="rotate(12 36 36)" />
      <rect x="22" y="22" width="28" height="28" transform="rotate(12 36 36)" opacity="0.45" />
    </g>,
    <g key="3" stroke="currentColor" strokeWidth="1" fill="none">
      <path d="M16 48 L36 12 L56 48" />
      <line x1="24" y1="36" x2="48" y2="36" />
      <line x1="28" y1="44" x2="44" y2="44" opacity="0.5" />
    </g>,
  ]
  return (
    <svg
      className={className}
      viewBox="0 0 72 64"
      xmlns="http://www.w3.org/2000/svg"
      aria-hidden
    >
      {paths[variant % paths.length]}
    </svg>
  )
}

export function SectionRule({ className = '' }) {
  return (
    <svg
      className={className}
      viewBox="0 0 1200 8"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      aria-hidden
      preserveAspectRatio="none"
    >
      <line
        x1="0"
        y1="4"
        x2="1200"
        y2="4"
        stroke="currentColor"
        strokeOpacity="0.2"
        strokeWidth="1"
      />
      <line x1="0" y1="4" x2="120" y2="4" stroke="currentColor" strokeWidth="1" />
    </svg>
  )
}
