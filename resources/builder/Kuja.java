package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Kuja extends _Default{
	public Kuja() {
		super("Kuja", null);
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{5503};
				break;
			case HP:
				ids = new int[]{5400};
				break;
			case S1:
				ids = new int[]{5404, 5403};
				break;
			case S2:
				ids = new int[]{5408, 5409};
				break;
			case EX:
				ids = new int[]{5399};
				break;
			case AA:
				ids = new int[]{5350};
				break;
			case LD:
				ids = new int[]{9472};
				break;
			case BT:
				ids = new int[]{9473};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}