param(
    [string]$UpstreamRoadmapsRoot = "C:\Users\user\VSProjects\AdapriveLearningNavigator\.tmp\roadmap-sh-upstream\src\data\roadmaps",
    [string]$OutputRoot = "C:\Users\user\VSProjects\AdapriveLearningNavigator\src\main\resources\roadmap-sh\roadmaps",
    [string]$CatalogPath = "C:\Users\user\VSProjects\AdapriveLearningNavigator\src\main\resources\roadmap-sh\catalog.json",
    [string]$LocalizationPath = "C:\Users\user\VSProjects\AdapriveLearningNavigator\tools\roadmap-sh-localization.json",
    [switch]$OverwriteExisting
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$preservedRoleCodes = @(
    "game-developer",
    "java-backend",
    "java-mobile"
)

$resourceTypeMap = @{
    "article" = "ARTICLE"
    "articles" = "ARTICLE"
    "official" = "ARTICLE"
    "offical" = "ARTICLE"
    "documentation" = "ARTICLE"
    "guide" = "ARTICLE"
    "community" = "ARTICLE"
    "dataset" = "ARTICLE"
    "framework" = "ARTICLE"
    "group" = "ARTICLE"
    "opensource" = "ARTICLE"
    "opensources" = "ARTICLE"
    "paper" = "ARTICLE"
    "platform" = "ARTICLE"
    "policy" = "ARTICLE"
    "report" = "ARTICLE"
    "research" = "ARTICLE"
    "roadmap" = "ARTICLE"
    "roadmap.sh" = "ARTICLE"
    "standard" = "ARTICLE"
    "tool" = "ARTICLE"
    "feed" = "ARTICLE"
    "book" = "BOOK"
    "course" = "COURSE"
    "video" = "VIDEO"
    "youtube" = "VIDEO"
    "conference" = "VIDEO"
    "podcast" = "VIDEO"
}

$providerMap = @{
    "developer.mozilla.org" = "MDN Web Docs"
    "docs.python.org" = "Python Docs"
    "go.dev" = "Go"
    "kubernetes.io" = "Kubernetes"
    "learn.microsoft.com" = "Microsoft Learn"
    "learn.unity.com" = "Unity Learn"
    "nodejs.org" = "Node.js"
    "php.net" = "PHP"
    "postgresql.org" = "PostgreSQL"
    "python.org" = "Python"
    "react.dev" = "React"
    "roadmap.sh" = "roadmap.sh"
    "rubyonrails.org" = "Ruby on Rails"
    "ruby-lang.org" = "Ruby"
    "rust-lang.org" = "Rust"
    "swift.org" = "Swift"
    "terraform.io" = "Terraform"
    "typescriptlang.org" = "TypeScript"
    "youtube.com" = "YouTube"
    "youtu.be" = "YouTube"
}

function Remove-Quotes {
    param([string]$Value)

    if ([string]::IsNullOrWhiteSpace($Value)) {
        return $Value
    }

    $trimmed = $Value.Trim()
    if (($trimmed.StartsWith("'") -and $trimmed.EndsWith("'")) -or ($trimmed.StartsWith('"') -and $trimmed.EndsWith('"'))) {
        return $trimmed.Substring(1, $trimmed.Length - 2)
    }

    return $trimmed
}

function Read-Frontmatter {
    param([string]$MarkdownPath)

    $frontmatter = @{}
    if (-not (Test-Path $MarkdownPath)) {
        return $frontmatter
    }

    $content = Get-Content $MarkdownPath -Raw -Encoding UTF8
    $match = [regex]::Match($content, "(?s)^---\s*\r?\n(?<body>.*?)\r?\n---")
    if (-not $match.Success) {
        return $frontmatter
    }

    foreach ($line in ($match.Groups["body"].Value -split "\r?\n")) {
        if ($line -match "^(?<key>[A-Za-z0-9_-]+):\s*(?<value>.+?)\s*$") {
            $frontmatter[$Matches["key"]] = Remove-Quotes $Matches["value"]
        }
    }

    return $frontmatter
}

function Convert-ToToken {
    param(
        [string]$Value,
        [int]$MaxLength = 20,
        [switch]$KeepUnderscore
    )

    if ([string]::IsNullOrWhiteSpace($Value)) {
        return "ITEM"
    }

    $pattern = if ($KeepUnderscore) { "[^A-Za-z0-9]+" } else { "[^A-Za-z0-9_]+" }
    $token = [regex]::Replace($Value.ToUpperInvariant(), $pattern, "_")
    $token = [regex]::Replace($token, "_{2,}", "_").Trim("_")

    if ([string]::IsNullOrWhiteSpace($token)) {
        $token = "ITEM"
    }

    if ($token.Length -gt $MaxLength) {
        $token = $token.Substring(0, $MaxLength)
    }

    return $token
}

function Get-ShortStableHash {
    param(
        [string]$Value,
        [int]$Length = 8
    )

    $sha1 = [System.Security.Cryptography.SHA1]::Create()
    try {
        $bytes = [System.Text.Encoding]::UTF8.GetBytes($Value)
        $hashBytes = $sha1.ComputeHash($bytes)
        $hash = [System.BitConverter]::ToString($hashBytes).Replace("-", "").ToUpperInvariant()
        if ($hash.Length -gt $Length) {
            return $hash.Substring(0, $Length)
        }
        return $hash
    } finally {
        $sha1.Dispose()
    }
}

function Get-StableTopicCode {
    param(
        [string]$RoleCode,
        [string]$NodeId
    )

    $roleToken = Convert-ToToken -Value $RoleCode -MaxLength 14 -KeepUnderscore
    $nodeToken = Convert-ToToken -Value $NodeId -MaxLength 18
    $hashToken = Get-ShortStableHash -Value ($RoleCode + ":" + $NodeId) -Length 8
    $code = "RM_{0}_{1}_{2}" -f $roleToken, $nodeToken, $hashToken

    if ($code.Length -gt 50) {
        $code = $code.Substring(0, 50)
    }

    return $code
}

function Get-HumanizedName {
    param([string]$Slug)

    if ([string]::IsNullOrWhiteSpace($Slug)) {
        return "Roadmap"
    }

    $parts = $Slug -split "-"
    $humanized = foreach ($part in $parts) {
        if ([string]::IsNullOrWhiteSpace($part)) {
            continue
        }

        switch ($part.ToLowerInvariant()) {
            "ai" { "AI"; continue }
            "qa" { "QA"; continue }
            "ios" { "iOS"; continue }
            "sql" { "SQL"; continue }
            "html" { "HTML"; continue }
            "css" { "CSS"; continue }
            "aws" { "AWS"; continue }
            "golang" { "Go"; continue }
            "nextjs" { "Next.js"; continue }
            "vue" { "Vue"; continue }
            "mlops" { "MLOps"; continue }
            "devsecops" { "DevSecOps"; continue }
            "cpp" { "C++"; continue }
            "aspnet" { "ASP.NET"; continue }
            "aspnet-core" { "ASP.NET Core"; continue }
            default {
                $part.Substring(0, 1).ToUpperInvariant() + $part.Substring(1)
            }
        }
    }

    return ($humanized -join " ").Trim()
}

function Get-Category {
    param(
        [string]$RoleCode,
        [string]$RoleName
    )

    $normalized = $RoleCode.ToLowerInvariant()
    if ($normalized -match "frontend|backend|full-stack|api|web|react|angular|nextjs|html|css|javascript|nodejs") {
        return "web"
    }
    if ($normalized -match "java|python|golang|ruby|rust|php|scala|cpp|kotlin|swift|shell-bash|sql") {
        return "language"
    }
    if ($normalized -match "devops|docker|kubernetes|terraform|cloudflare|aws|mlops|devsecops") {
        return "devops"
    }
    if ($normalized -match "spring-boot|django|laravel|flutter|react-native|aspnet-core|swift-ui") {
        return "framework"
    }
    if ($normalized -match "system-design|software-design|software-architect|design-system|api-design") {
        return "architecture"
    }
    if ($normalized -match "computer-science|datastructures|leetcode|linux|git-github") {
        return "fundamentals"
    }
    if ($normalized -match "data|machine-learning|ai-|prompt-engineering") {
        return "data"
    }
    return "roadmap"
}

function Get-Language {
    param([string]$Url)

    if ([string]::IsNullOrWhiteSpace($Url)) {
        return "en"
    }

    if ($Url -match "://[^/]*\.ru([/:?]|$)" -or
        $Url -match "/ru(/|$)" -or
        $Url -match "[?&](lang|locale)=ru([&]|$)" -or
        $Url -match "/ru-[A-Za-z]{2}(/|$)") {
        return "ru"
    }

    return "en"
}

function Get-Provider {
    param([string]$Url)

    if ([string]::IsNullOrWhiteSpace($Url)) {
        return "External Resource"
    }

    if ($Url -notmatch '^[a-z]+://(?<hostValue>[^/]+)') {
        return "External Resource"
    }

    $providerHost = $Matches["hostValue"].ToLowerInvariant()
    if ($providerHost.Contains(":")) {
        $providerHost = $providerHost.Split(":")[0]
    }
    if ($providerHost.StartsWith("www.")) {
        $providerHost = $providerHost.Substring(4)
    }

    if ($providerMap.ContainsKey($providerHost)) {
        return $providerMap[$providerHost]
    }

    $parts = $providerHost -split "\."
    if ($parts.Length -ge 2) {
        $root = $parts[$parts.Length - 2]
        if ($root.Length -gt 0) {
            return $root.Substring(0, 1).ToUpperInvariant() + $root.Substring(1)
        }
    }

    return $providerHost
}

function Get-ResourceType {
    param(
        [string]$Kind,
        [string]$Url
    )

    $normalizedKind = if ([string]::IsNullOrWhiteSpace($Kind)) { "" } else { $Kind.ToLowerInvariant() }

    if ($resourceTypeMap.ContainsKey($normalizedKind)) {
        return $resourceTypeMap[$normalizedKind]
    }

    if ($Url -match "youtube\.com|youtu\.be") {
        return "VIDEO"
    }

    return "ARTICLE"
}

function Get-DurationMinutes {
    param(
        [string]$ResourceType,
        [string]$Url
    )

    switch ($ResourceType) {
        "VIDEO" { return 25 }
        "COURSE" { return 180 }
        "BOOK" { return 240 }
        "INTERACTIVE" { return 60 }
        default {
            if ($Url -match "roadmap\.sh/courses/") {
                return 180
            }
            return 35
        }
    }
}

function Get-TopicLevel {
    param(
        [string]$RoleCode,
        [string]$NodeType,
        [int]$Depth,
        [string]$Title
    )

    if ($RoleCode -match "beginner") {
        return "BASIC"
    }

    if ($Title -match "(?i)\badvanced\b|\binternals\b|\bdeep dive\b") {
        return "ADVANCED"
    }

    if ($Depth -ge 3) {
        return "ADVANCED"
    }

    if ($Depth -eq 2 -or $NodeType -eq "subtopic") {
        return "INTERMEDIATE"
    }

    return "BASIC"
}

function Get-EstimatedHours {
    param(
        [string]$Level,
        [string]$NodeType,
        [int]$ResourceCount
    )

    [int]$base = 4
    if ($Level -eq "ADVANCED") {
        $base = 8
    } elseif ($Level -eq "INTERMEDIATE") {
        $base = 6
    }

    if ($NodeType -eq "topic") {
        $base += 1
    }

    if ($ResourceCount -ge 3) {
        $base += 1
    }
    if ($ResourceCount -ge 6) {
        $base += 1
    }

    if ($base -gt 10) {
        $base = 10
    }

    return $base
}

function Get-RelationType {
    param([object]$Edge)

    if ($null -ne $Edge.data -and [string]$Edge.data.edgeStyle -eq "dashed") {
        return "RECOMMENDED"
    }

    if ($null -ne $Edge.style -and $null -ne $Edge.style.strokeDasharray) {
        $dash = [string]$Edge.style.strokeDasharray
        if (-not [string]::IsNullOrWhiteSpace($dash) -and $dash -ne "0") {
            return "RECOMMENDED"
        }
    }

    return "REQUIRED"
}

function Parse-ContentResources {
    param([string]$ContentPath)

    $resources = New-Object System.Collections.Generic.List[object]
    if ([string]::IsNullOrWhiteSpace($ContentPath) -or -not (Test-Path $ContentPath)) {
        return @()
    }

    $rank = 1
    $seenUrls = [System.Collections.Generic.HashSet[string]]::new([System.StringComparer]::OrdinalIgnoreCase)
    foreach ($line in Get-Content $ContentPath -Encoding UTF8) {
        if ($line -match '^\s*-\s+\[@(?<kind>[^@]+)@(?<title>.+?)\]\((?<url>https?://.+?)\)\s*$') {
            $url = $Matches["url"].Trim()
            if (-not $seenUrls.Add($url)) {
                continue
            }

            $resourceType = Get-ResourceType -Kind $Matches["kind"] -Url $url
            $language = Get-Language -Url $url
            $resource = [ordered]@{
                title = $Matches["title"].Trim()
                url = $url
                type = $resourceType
                language = $language
                durationMinutes = Get-DurationMinutes -ResourceType $resourceType -Url $url
                provider = Get-Provider -Url $url
                difficulty = "BEGINNER"
                status = "ACTIVE"
                rank = $rank
            }
            $rank++
            $resources.Add([pscustomobject]$resource)
        }
    }

    return @($resources.ToArray())
}

function Get-NodeDepthMap {
    param([string]$RoadmapDir)

    $depthByNodeId = @{}
    foreach ($mappingFileName in @("migration-mapping.json", "mapping.json")) {
        $mappingPath = Join-Path $RoadmapDir $mappingFileName
        if (-not (Test-Path $mappingPath)) {
            continue
        }

        $mappingJson = Get-Content $mappingPath -Raw -Encoding UTF8 | ConvertFrom-Json
        foreach ($entry in $mappingJson.PSObject.Properties) {
            $depth = (($entry.Name -split ":").Count)
            $nodeId = [string]$entry.Value
            if (-not $depthByNodeId.ContainsKey($nodeId) -or $depthByNodeId[$nodeId] -lt $depth) {
                $depthByNodeId[$nodeId] = $depth
            }
        }
    }

    return $depthByNodeId
}

function Get-TopologicalOrder {
    param(
        [hashtable]$TopicNodesById,
        [System.Collections.Generic.List[object]]$SelectedEdges
    )

    $indegree = @{}
    $outgoing = @{}
    foreach ($nodeId in $TopicNodesById.Keys) {
        $indegree[$nodeId] = 0
        $outgoing[$nodeId] = New-Object System.Collections.Generic.List[string]
    }

    foreach ($edge in $SelectedEdges) {
        $outgoing[$edge.source].Add($edge.target)
        $indegree[$edge.target]++
    }

    $sortNodes = {
        param([object[]]$NodeIds)
        return $NodeIds | Sort-Object `
            @{ Expression = { [double]$TopicNodesById[$_].position.y } }, `
            @{ Expression = { [double]$TopicNodesById[$_].position.x } }, `
            @{ Expression = { [string]$TopicNodesById[$_].data.label } }
    }

    $queue = New-Object System.Collections.Generic.List[string]
    foreach ($nodeId in (& $sortNodes ($TopicNodesById.Keys | Where-Object { $indegree[$_] -eq 0 }))) {
        $queue.Add($nodeId)
    }

    $ordered = New-Object System.Collections.Generic.List[string]
    while ($queue.Count -gt 0) {
        $current = $queue[0]
        $queue.RemoveAt(0)
        $ordered.Add($current)

        foreach ($target in ($outgoing[$current] | Sort-Object)) {
            $indegree[$target]--
            if ($indegree[$target] -eq 0) {
                $queue.Add($target)
                $sortedQueue = & $sortNodes @($queue.ToArray())
                $queue.Clear()
                foreach ($queuedNode in $sortedQueue) {
                    $queue.Add($queuedNode)
                }
            }
        }
    }

    if ($ordered.Count -lt $TopicNodesById.Count) {
        foreach ($remainingNode in (& $sortNodes ($TopicNodesById.Keys | Where-Object { -not $ordered.Contains($_) }))) {
            $ordered.Add($remainingNode)
        }
    }

    return $ordered
}

function Get-OptionalProperty {
    param(
        [object]$Object,
        [string]$PropertyName,
        $DefaultValue = $null
    )

    if ($null -eq $Object) {
        return $DefaultValue
    }

    $propertyNames = @($Object.PSObject.Properties.Name)
    if ($propertyNames -contains $PropertyName) {
        return $Object.$PropertyName
    }

    return $DefaultValue
}

function Normalize-LegacyControlName {
    param([string]$ControlName)

    if ([string]::IsNullOrWhiteSpace($ControlName)) {
        return ""
    }

    $segments = $ControlName -split ":"
    $normalizedSegments = foreach ($segment in $segments) {
        $normalizedSegment = ($segment -replace '^\d+-', '') -replace '^check-?', ''
        if (-not [string]::IsNullOrWhiteSpace($normalizedSegment)) {
            $normalizedSegment
        }
    }

    return ($normalizedSegments -join ":").Trim(":")
}

function Get-Slug {
    param([string]$Value)

    if ([string]::IsNullOrWhiteSpace($Value)) {
        return ""
    }

    $slug = [regex]::Replace($Value.ToLowerInvariant(), '[^a-z0-9]+', '-')
    return $slug.Trim('-')
}

function Get-LegacyGroupLabel {
    param(
        [object]$GroupControl,
        [string]$NormalizedKey
    )

    $children = Get-OptionalProperty -Object $GroupControl -PropertyName "children"
    $controlsNode = Get-OptionalProperty -Object $children -PropertyName "controls"
    $childControls = @(Get-OptionalProperty -Object $controlsNode -PropertyName "control" -DefaultValue @())
    foreach ($childControl in $childControls) {
        if ([string](Get-OptionalProperty -Object $childControl -PropertyName "typeID" -DefaultValue "") -ne "Label") {
            continue
        }

        $properties = Get-OptionalProperty -Object $childControl -PropertyName "properties"
        $label = [string](Get-OptionalProperty -Object $properties -PropertyName "text" -DefaultValue "")
        if (-not [string]::IsNullOrWhiteSpace($label)) {
            return $label
        }
    }

    $lastSegment = ($NormalizedKey -split ":")[-1]
    return Get-HumanizedName -Slug $lastSegment
}

function Collect-LegacyGroupControls {
    param(
        [object[]]$Controls,
        [System.Collections.Generic.List[object]]$Groups
    )

    foreach ($control in @($Controls)) {
        $typeId = [string](Get-OptionalProperty -Object $control -PropertyName "typeID" -DefaultValue "")
        $properties = Get-OptionalProperty -Object $control -PropertyName "properties"
        $controlName = [string](Get-OptionalProperty -Object $properties -PropertyName "controlName" -DefaultValue "")

        if ($typeId -eq "__group__" -and
            -not [string]::IsNullOrWhiteSpace($controlName) -and
            -not $controlName.StartsWith("ext_link")) {
            $Groups.Add($control)
        }

        $children = Get-OptionalProperty -Object $control -PropertyName "children"
        $controlsNode = Get-OptionalProperty -Object $children -PropertyName "controls"
        $childControls = @(Get-OptionalProperty -Object $controlsNode -PropertyName "control" -DefaultValue @())
        if ($childControls.Count -gt 0) {
            Collect-LegacyGroupControls -Controls $childControls -Groups $Groups
        }
    }
}

function Get-LegacyContentItems {
    param([string]$ContentDir)

    $items = New-Object System.Collections.Generic.List[object]
    if (-not (Test-Path $ContentDir)) {
        return @()
    }

    foreach ($contentFile in (Get-ChildItem $ContentDir -Filter "*.md")) {
        if ($contentFile.Name -match '^(?<slug>.+?)@(?<nodeId>[^@]+)\.md$') {
            $slug = $Matches["slug"]
            $tokens = @($slug -split '-' | Where-Object { -not [string]::IsNullOrWhiteSpace($_) })
            $items.Add([pscustomobject]@{
                slug = $slug
                nodeId = $Matches["nodeId"]
                path = $contentFile.FullName
                tokens = $tokens
            })
        }
    }

    return @($items.ToArray())
}

function Resolve-LegacyContentItem {
    param(
        [string]$NormalizedKey,
        [string]$Title,
        [object[]]$ContentItems,
        [System.Collections.Generic.HashSet[string]]$AssignedNodeIds
    )

    if (-not $ContentItems -or $ContentItems.Count -eq 0) {
        return $null
    }

    $lastSegment = ($NormalizedKey -split ":")[-1]
    $candidateSlugs = @(
        Get-Slug -Value $lastSegment
        Get-Slug -Value ($NormalizedKey -replace ":", "-")
        Get-Slug -Value $Title
    ) | Where-Object { -not [string]::IsNullOrWhiteSpace($_) } | Select-Object -Unique

    foreach ($candidateSlug in $candidateSlugs) {
        $exactMatch = $ContentItems | Where-Object {
            -not $AssignedNodeIds.Contains([string]$_.nodeId) -and [string]$_.slug -eq $candidateSlug
        } | Select-Object -First 1
        if ($null -ne $exactMatch) {
            return $exactMatch
        }
    }

    $candidateTokens = @($candidateSlugs | ForEach-Object { $_ -split "-" } | Where-Object { -not [string]::IsNullOrWhiteSpace($_) } | Select-Object -Unique)
    if ($candidateTokens.Count -eq 0) {
        return $null
    }

    $bestItem = $null
    $bestScore = 0
    foreach ($contentItem in $ContentItems) {
        if ($AssignedNodeIds.Contains([string]$contentItem.nodeId)) {
            continue
        }

        $overlap = @($contentItem.tokens | Where-Object { $candidateTokens -contains $_ }).Count
        if ($overlap -le 0) {
            continue
        }

        $bonus = 0
        if ([string]$contentItem.slug -like "*$($candidateSlugs[0])*" -or $candidateSlugs[0] -like "*$([string]$contentItem.slug)*") {
            $bonus = 1
        }

        $score = ($overlap * 10) + $bonus - ([string]$contentItem.slug).Length / 100.0
        if ($score -gt $bestScore) {
            $bestScore = $score
            $bestItem = $contentItem
        }
    }

    return $bestItem
}

function Build-LegacyNormalizedRoadmap {
    param(
        [object]$RoadmapData,
        [string]$RoadmapDirPath
    )

    $mockup = Get-OptionalProperty -Object $RoadmapData -PropertyName "mockup"
    $controlsNode = Get-OptionalProperty -Object (Get-OptionalProperty -Object $mockup -PropertyName "controls") -PropertyName "control" -DefaultValue @()
    $groups = New-Object System.Collections.Generic.List[object]
    Collect-LegacyGroupControls -Controls @($controlsNode) -Groups $groups

    $depthByNodeId = @{}
    $groupKeyToNodeId = @{}
    $topicNodesById = @{}
    $contentByNodeId = @{}
    $selectedEdges = New-Object System.Collections.Generic.List[object]

    $mappingByKey = @{}
    $mappingPath = Join-Path $RoadmapDirPath "migration-mapping.json"
    if (Test-Path $mappingPath) {
        $mappingJson = Get-Content $mappingPath -Raw -Encoding UTF8 | ConvertFrom-Json
        foreach ($entry in $mappingJson.PSObject.Properties) {
            $mappingByKey[[string]$entry.Name] = [string]$entry.Value
        }
    }

    $contentItems = Get-LegacyContentItems -ContentDir (Join-Path $RoadmapDirPath "content")
    $assignedNodeIds = [System.Collections.Generic.HashSet[string]]::new([System.StringComparer]::OrdinalIgnoreCase)

    $legacyTopicEntries = New-Object System.Collections.Generic.List[object]
    foreach ($group in $groups) {
        $properties = Get-OptionalProperty -Object $group -PropertyName "properties"
        $controlName = [string](Get-OptionalProperty -Object $properties -PropertyName "controlName" -DefaultValue "")
        $normalizedKey = Normalize-LegacyControlName -ControlName $controlName
        if ([string]::IsNullOrWhiteSpace($normalizedKey)) {
            continue
        }

        $label = Get-LegacyGroupLabel -GroupControl $group -NormalizedKey $normalizedKey
        $depth = ($normalizedKey -split ":").Count
        $legacyTopicEntries.Add([pscustomobject]@{
            normalizedKey = $normalizedKey
            label = $label
            depth = $depth
            x = [double](Get-OptionalProperty -Object $group -PropertyName "x" -DefaultValue 0)
            y = [double](Get-OptionalProperty -Object $group -PropertyName "y" -DefaultValue 0)
        }) | Out-Null
    }

    foreach ($entry in ($legacyTopicEntries | Sort-Object y, x, normalizedKey)) {
        $nodeId = if ($mappingByKey.ContainsKey($entry.normalizedKey)) {
            $mappingByKey[$entry.normalizedKey]
        } else {
            $matchedContent = Resolve-LegacyContentItem -NormalizedKey $entry.normalizedKey -Title $entry.label -ContentItems $contentItems -AssignedNodeIds $assignedNodeIds
            if ($null -ne $matchedContent) {
                $assignedNodeIds.Add([string]$matchedContent.nodeId) | Out-Null
                $contentByNodeId[[string]$matchedContent.nodeId] = [string]$matchedContent.path
                [string]$matchedContent.nodeId
            } else {
                "LEGACY_" + (Convert-ToToken -Value $entry.normalizedKey -MaxLength 36 -KeepUnderscore)
            }
        }

        if ($mappingByKey.ContainsKey($entry.normalizedKey)) {
            $matchedById = $contentItems | Where-Object { [string]$_.nodeId -eq $nodeId } | Select-Object -First 1
            if ($null -ne $matchedById) {
                $contentByNodeId[$nodeId] = [string]$matchedById.path
                $assignedNodeIds.Add([string]$matchedById.nodeId) | Out-Null
            }
        }

        $groupKeyToNodeId[$entry.normalizedKey] = $nodeId
        $depthByNodeId[$nodeId] = [int]$entry.depth
        $topicNodesById[$nodeId] = [pscustomobject]@{
            id = $nodeId
            type = $(if ($entry.depth -gt 1) { "subtopic" } else { "topic" })
            position = [pscustomobject]@{
                x = $entry.x
                y = $entry.y
            }
            data = [pscustomobject]@{
                label = $entry.label
            }
        }
    }

    foreach ($entry in $legacyTopicEntries) {
        $normalizedKey = [string]$entry.normalizedKey
        $segments = $normalizedKey -split ":"
        if ($segments.Count -le 1) {
            continue
        }

        $parentKey = ($segments[0..($segments.Count - 2)] -join ":")
        if (-not $groupKeyToNodeId.ContainsKey($parentKey)) {
            continue
        }

        $selectedEdges.Add([pscustomobject]@{
            source = $groupKeyToNodeId[$parentKey]
            target = $groupKeyToNodeId[$normalizedKey]
            relationType = "REQUIRED"
        }) | Out-Null
    }

    return [pscustomobject]@{
        topicNodesById = $topicNodesById
        selectedEdges = @($selectedEdges.ToArray())
        contentByNodeId = $contentByNodeId
        depthByNodeId = $depthByNodeId
    }
}

function Get-MarkdownHeading {
    param([string]$MarkdownPath)

    if (-not (Test-Path $MarkdownPath)) {
        return ""
    }

    foreach ($line in Get-Content $MarkdownPath -Encoding UTF8) {
        if ($line -match '^#\s+(?<heading>.+?)\s*$') {
            return $Matches["heading"].Trim()
        }
    }

    return ""
}

function Build-ContentOnlyNormalizedRoadmap {
    param([string]$RoadmapDirPath)

    $topicNodesById = @{}
    $contentByNodeId = @{}
    $depthByNodeId = @{}
    $selectedEdges = @()

    $contentItems = Get-LegacyContentItems -ContentDir (Join-Path $RoadmapDirPath "content")
    $positionY = 0
    foreach ($contentItem in ($contentItems | Sort-Object slug)) {
        $title = Get-MarkdownHeading -MarkdownPath $contentItem.path
        if ([string]::IsNullOrWhiteSpace($title)) {
            $title = Get-HumanizedName -Slug ([string]$contentItem.slug)
        }

        $topicNodesById[[string]$contentItem.nodeId] = [pscustomobject]@{
            id = [string]$contentItem.nodeId
            type = "topic"
            position = [pscustomobject]@{
                x = 0
                y = $positionY
            }
            data = [pscustomobject]@{
                label = $title
            }
        }
        $contentByNodeId[[string]$contentItem.nodeId] = [string]$contentItem.path
        $depthByNodeId[[string]$contentItem.nodeId] = 1
        $positionY += 100
    }

    return [pscustomobject]@{
        topicNodesById = $topicNodesById
        selectedEdges = $selectedEdges
        contentByNodeId = $contentByNodeId
        depthByNodeId = $depthByNodeId
    }
}

if (-not (Test-Path $UpstreamRoadmapsRoot)) {
    throw "Upstream roadmap.sh directory not found: $UpstreamRoadmapsRoot"
}
if (-not (Test-Path $LocalizationPath)) {
    throw "Localization file not found: $LocalizationPath"
}

New-Item -ItemType Directory -Path $OutputRoot -Force | Out-Null
$localization = Get-Content $LocalizationPath -Raw -Encoding UTF8 | ConvertFrom-Json

$existingCatalogEntries = @{}
if (Test-Path $CatalogPath) {
    $existingCatalog = Get-Content $CatalogPath -Raw -Encoding UTF8 | ConvertFrom-Json
    foreach ($entry in $existingCatalog.entries) {
        $existingCatalogEntries[[string]$entry.code] = [string]$entry.category
    }
}

$generatedManifestPaths = New-Object System.Collections.Generic.List[string]
$generatedRoleCodes = [System.Collections.Generic.HashSet[string]]::new([System.StringComparer]::OrdinalIgnoreCase)

foreach ($roadmapDir in (Get-ChildItem $UpstreamRoadmapsRoot -Directory | Sort-Object Name)) {
    $roleCode = $roadmapDir.Name
    $outputPath = Join-Path $OutputRoot ($roleCode + ".json")
    $contentDir = Join-Path $roadmapDir.FullName "content"

    if ($preservedRoleCodes -contains $roleCode) {
        if (Test-Path $outputPath) {
            $generatedManifestPaths.Add($outputPath) | Out-Null
            $generatedRoleCodes.Add($roleCode) | Out-Null
        }
        continue
    }

    if (-not $OverwriteExisting -and (Test-Path $outputPath)) {
        $generatedManifestPaths.Add($outputPath) | Out-Null
        $generatedRoleCodes.Add($roleCode) | Out-Null
        continue
    }

    $roadmapJsonPath = Join-Path $roadmapDir.FullName ($roleCode + ".json")
    $frontmatter = Read-Frontmatter -MarkdownPath (Join-Path $roadmapDir.FullName ($roleCode + ".md"))
    $roleName = if ($frontmatter.ContainsKey("title") -and -not [string]::IsNullOrWhiteSpace($frontmatter["title"])) {
        [string]$frontmatter["title"]
    } else {
        Get-HumanizedName -Slug $roleCode
    }

    $topicNodesById = @{}
    $selectedEdges = New-Object System.Collections.Generic.List[object]
    $contentByNodeId = @{}
    $depthByNodeId = @{}

    if (Test-Path $roadmapJsonPath) {
        $roadmapData = Get-Content $roadmapJsonPath -Raw -Encoding UTF8 | ConvertFrom-Json
        $roadmapPropertyNames = @($roadmapData.PSObject.Properties.Name)
    } else {
        $roadmapData = $null
        $roadmapPropertyNames = @()
    }

    if ($roadmapPropertyNames -contains "nodes") {
        $topicNodes = $roadmapData.nodes | Where-Object {
            $_.type -in @("topic", "subtopic") -and
            $null -ne $_.data -and
            -not [string]::IsNullOrWhiteSpace([string]$_.data.label)
        }

        foreach ($node in $topicNodes) {
            $topicNodesById[[string]$node.id] = $node
        }

        $roadmapEdges = if ($roadmapPropertyNames -contains "edges") { @($roadmapData.edges) } else { @() }
        foreach ($edge in $roadmapEdges) {
            $edgePropertyNames = @($edge.PSObject.Properties.Name)
            if (-not ($edgePropertyNames -contains "source") -or -not ($edgePropertyNames -contains "target")) {
                continue
            }
            $sourceId = [string]$edge.source
            $targetId = [string]$edge.target
            if ($topicNodesById.ContainsKey($sourceId) -and $topicNodesById.ContainsKey($targetId) -and $sourceId -ne $targetId) {
                $selectedEdges.Add([pscustomobject]@{
                    source = $sourceId
                    target = $targetId
                    relationType = Get-RelationType -Edge $edge
                }) | Out-Null
            }
        }

        if (Test-Path $contentDir) {
            foreach ($contentFile in (Get-ChildItem $contentDir -Filter "*.md")) {
                if ($contentFile.Name -match '@(?<nodeId>[^@]+)\.md$') {
                    $contentByNodeId[$Matches["nodeId"]] = $contentFile.FullName
                }
            }
        }

        $depthByNodeId = Get-NodeDepthMap -RoadmapDir $roadmapDir.FullName
    } elseif ($roadmapPropertyNames -contains "mockup") {
        $legacyRoadmap = Build-LegacyNormalizedRoadmap -RoadmapData $roadmapData -RoadmapDirPath $roadmapDir.FullName
        $topicNodesById = $legacyRoadmap.topicNodesById
        $selectedEdges = New-Object System.Collections.Generic.List[object]
        foreach ($edge in @($legacyRoadmap.selectedEdges)) {
            $selectedEdges.Add($edge) | Out-Null
        }
        $contentByNodeId = $legacyRoadmap.contentByNodeId
        $depthByNodeId = $legacyRoadmap.depthByNodeId
    } elseif (Test-Path $contentDir) {
        $contentOnlyRoadmap = Build-ContentOnlyNormalizedRoadmap -RoadmapDirPath $roadmapDir.FullName
        $topicNodesById = $contentOnlyRoadmap.topicNodesById
        $contentByNodeId = $contentOnlyRoadmap.contentByNodeId
        $depthByNodeId = $contentOnlyRoadmap.depthByNodeId
    }

    if ($topicNodesById.Count -eq 0 -and (Test-Path $contentDir)) {
        $contentOnlyRoadmap = Build-ContentOnlyNormalizedRoadmap -RoadmapDirPath $roadmapDir.FullName
        $topicNodesById = $contentOnlyRoadmap.topicNodesById
        $selectedEdges = New-Object System.Collections.Generic.List[object]
        $contentByNodeId = $contentOnlyRoadmap.contentByNodeId
        $depthByNodeId = $contentOnlyRoadmap.depthByNodeId
    }

    if ($topicNodesById.Count -eq 0) {
        continue
    }

    $orderedNodeIds = Get-TopologicalOrder -TopicNodesById $topicNodesById -SelectedEdges $selectedEdges

    $topicCodeByNodeId = @{}
    foreach ($nodeId in $orderedNodeIds) {
        $topicCodeByNodeId[$nodeId] = Get-StableTopicCode -RoleCode $roleCode -NodeId $nodeId
    }

    $manifestTopics = New-Object System.Collections.Generic.List[object]
    $priority = 1
    foreach ($nodeId in $orderedNodeIds) {
        $node = $topicNodesById[$nodeId]
        $resources = @(Parse-ContentResources -ContentPath $contentByNodeId[$nodeId])
        $nodeType = [string]$node.type
        [int]$resourceCount = $resources.Count
        $depth = if ($depthByNodeId.ContainsKey($nodeId)) { [int]$depthByNodeId[$nodeId] } elseif ([string]$node.type -eq "subtopic") { 2 } else { 1 }
        $topicLevel = Get-TopicLevel -RoleCode $roleCode -NodeType $nodeType -Depth $depth -Title ([string]$node.data.label)
        $resourceDifficulty = switch ($topicLevel) {
            "ADVANCED" { "ADVANCED" }
            "INTERMEDIATE" { "INTERMEDIATE" }
            default { "BEGINNER" }
        }

        foreach ($resource in $resources) {
            $resource.difficulty = $resourceDifficulty
        }

        $topicKind = if ($nodeType -eq "subtopic") { $localization.topicKindSubtopic } else { $localization.topicKindTopic }
        $topicResourcesSuffix = if ($resourceCount -gt 0) { $localization.topicDescriptionResourcesSuffix } else { "" }
        $topicDescription = "{0}{1}{2}{3}{4}.{5}" -f `
            $topicKind, `
            $localization.topicDescriptionMiddle, `
            $roleName, `
            $localization.topicDescriptionAfterRole, `
            ([string]$node.data.label), `
            $topicResourcesSuffix

        $prereqs = New-Object System.Collections.Generic.List[object]
        foreach ($edge in ($selectedEdges | Where-Object { $_.target -eq $nodeId } | Sort-Object source)) {
            $prereqs.Add([pscustomobject][ordered]@{
                topicCode = $topicCodeByNodeId[$edge.source]
                relationType = $edge.relationType
            })
        }

        $required = ($nodeType -eq "topic")
        [int]$estimatedHours = Get-EstimatedHours -Level $topicLevel -NodeType $nodeType -ResourceCount $resourceCount
        $prereqItems = @($prereqs.ToArray())
        $resourceItems = @($resources)
        $topicRecord = [ordered]@{
            code = $topicCodeByNodeId[$nodeId]
            title = [string]$node.data.label
            description = $topicDescription
            level = $topicLevel
            core = $required
            estimatedHours = $estimatedHours
            status = "ACTIVE"
            priority = $priority
            required = $required
            prereqs = $prereqItems
            resources = $resourceItems
        }
        $manifestTopics.Add([pscustomobject]$topicRecord) | Out-Null
        $priority++
    }

    $roleDescription = if ($roleCode -match "beginner") {
        "{0}{1}{2}" -f $localization.roleDescriptionStarterPrefix, $roleName, $localization.roleDescriptionSuffix
    } else {
        "{0}{1}{2}" -f $localization.roleDescriptionPrefix, $roleName, $localization.roleDescriptionSuffix
    }

    $manifestTopicItems = @($manifestTopics.ToArray())
    $manifestRecord = [ordered]@{
        source = "roadmap.sh"
        sourceUrl = "https://roadmap.sh/$roleCode"
        roleCode = $roleCode
        roleName = $roleName
        roleDescription = $roleDescription
        roleStatus = "ACTIVE"
        topics = $manifestTopicItems
    }
    $manifest = [pscustomobject]$manifestRecord

    $manifest | ConvertTo-Json -Depth 100 | Set-Content -Path $outputPath -Encoding utf8
    $generatedManifestPaths.Add($outputPath) | Out-Null
    $generatedRoleCodes.Add($roleCode) | Out-Null
}

$catalogEntries = New-Object System.Collections.Generic.List[object]
foreach ($manifestPath in (Get-ChildItem $OutputRoot -Filter "*.json" | Sort-Object Name)) {
    $manifest = Get-Content $manifestPath.FullName -Raw -Encoding UTF8 | ConvertFrom-Json
    $category = if ($existingCatalogEntries.ContainsKey([string]$manifest.roleCode)) {
        $existingCatalogEntries[[string]$manifest.roleCode]
    } else {
        Get-Category -RoleCode ([string]$manifest.roleCode) -RoleName ([string]$manifest.roleName)
    }

    $catalogEntries.Add([pscustomobject][ordered]@{
        code = [string]$manifest.roleCode
        name = [string]$manifest.roleName
        description = [string]$manifest.roleDescription
        category = $category
    }) | Out-Null
}

$catalogEntryItems = @($catalogEntries.ToArray())
$catalogRecord = [ordered]@{
    source = "roadmap.sh"
    sourceUrl = "https://roadmap.sh/"
    entries = $catalogEntryItems
}
$catalogManifest = [pscustomobject]$catalogRecord
$catalogManifest | ConvertTo-Json -Depth 20 | Set-Content -Path $CatalogPath -Encoding utf8

$totalTopics = 0
$totalResources = 0
foreach ($manifestPath in (Get-ChildItem $OutputRoot -Filter "*.json")) {
    $manifest = Get-Content $manifestPath.FullName -Raw -Encoding UTF8 | ConvertFrom-Json
    $totalTopics += ($manifest.topics | Measure-Object).Count
    foreach ($topic in $manifest.topics) {
        $totalResources += ($topic.resources | Measure-Object).Count
    }
}

Write-Host ("Generated {0} roadmap manifests, {1} topics and {2} resources." -f (Get-ChildItem $OutputRoot -Filter "*.json").Count, $totalTopics, $totalResources)
