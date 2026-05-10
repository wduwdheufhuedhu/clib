type SvgProps = { className?: string; 'aria-hidden'?: boolean }

export function HeroComposition({ className }: SvgProps) {
  return (
    <svg
      className={className}
      viewBox="0 0 520 420"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      aria-hidden
    >
      <defs>
        <pattern id="grid" width="40" height="40" patternUnits="userSpaceOnUse">
          <path d="M40 0H0V40" stroke="currentColor" strokeWidth="0.5" opacity="0.2" />
        </pattern>
      </defs>
      <rect width="520" height="420" fill="url(#grid)" />
      <circle cx="120" cy="100" r="72" stroke="currentColor" strokeWidth="1.25" />
      <rect
        x="260"
        y="48"
        width="200"
        height="200"
        stroke="currentColor"
        strokeWidth="1.25"
        transform="rotate(12 360 148)"
      />
      <path
        d="M48 320 L180 220 L320 340 L472 200"
        stroke="currentColor"
        strokeWidth="1.25"
        strokeLinecap="square"
      />
      <path
        d="M200 380 L380 280"
        stroke="currentColor"
        strokeWidth="1.25"
        strokeDasharray="6 10"
        opacity="0.45"
      />
      <polygon
        points="420,360 480,300 500,380"
        stroke="currentColor"
        strokeWidth="1.25"
        fill="none"
      />
    </svg>
  )
}

export function ThumbIso({ className }: SvgProps) {
  return (
    <svg className={className} viewBox="0 0 120 120" aria-hidden>
      <path
        d="M20 88 L60 28 L100 88 Z"
        stroke="currentColor"
        strokeWidth="1.2"
        fill="none"
      />
      <path d="M20 88 L60 108 L100 88" stroke="currentColor" strokeWidth="1.2" fill="none" />
      <path d="M60 28 L60 108" stroke="currentColor" strokeWidth="1.2" opacity="0.35" />
    </svg>
  )
}

export function ThumbOrbit({ className }: SvgProps) {
  return (
    <svg className={className} viewBox="0 0 120 120" aria-hidden>
      <circle cx="60" cy="60" r="36" stroke="currentColor" strokeWidth="1.2" fill="none" />
      <circle cx="60" cy="60" r="8" fill="currentColor" />
      <ellipse
        cx="60"
        cy="60"
        rx="52"
        ry="22"
        stroke="currentColor"
        strokeWidth="1"
        fill="none"
        transform="rotate(-24 60 60)"
        opacity="0.5"
      />
    </svg>
  )
}

export function ThumbStack({ className }: SvgProps) {
  return (
    <svg className={className} viewBox="0 0 120 120" aria-hidden>
      <rect x="24" y="68" width="72" height="28" stroke="currentColor" strokeWidth="1.2" fill="none" />
      <rect x="32" y="46" width="56" height="28" stroke="currentColor" strokeWidth="1.2" fill="none" />
      <rect x="40" y="24" width="40" height="28" stroke="currentColor" strokeWidth="1.2" fill="none" />
    </svg>
  )
}
